package community.flock.eco.feature.payment.services

import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.payment.model.PaymentTransaction
import community.flock.eco.feature.payment.model.PaymentTransactionStatus
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*
import java.util.Base64.getEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class PaymentBuckarooService(
        @Value("\${buckaroo.requestUri}")
        private val requestUri: String,

        @Value("\${buckaroo.websiteKey}")
        private val websiteKey: String,

        @Value("\${buckaroo.secretKey}")
        private val secretKey: String,

        private val paymentTransactionRepository: PaymentTransactionRepository
) {

    sealed class PaymentMethod {
        abstract val amount: Double

        data class Ideal(override val amount: Double, val description: String, val issuer: String) : PaymentMethod() {
            fun getContent(): String = """{
                "Currency": "EUR",
                "AmountDebit": $amount,
                "Invoice": "$description",
                "ClientIP": {
                    "Type": 0,
                    "Address": "0.0.0.0"
                },
                    "Services": {
                        "ServiceList": [
                        {
                            "Name": "ideal",
                            "Action": "Pay",
                            "Parameters": [
                            {
                                "Name": "issuer",
                                "Value": "$issuer"
                            }
                        ]
                    }
                ]
            }"""
        }

        data class CreditCard(override val amount: Double, val description: String, val issuer: String) : PaymentMethod() {
            fun getContent(): String = """{
              "Currency": "EUR",
              "AmountDebit": $amount,
              "Invoice": "$description",
              "ClientIP": {
                  "Type": 0,
                  "Address": "0.0.0.0"
               },
              "Services": {
                "ServiceList": [
                  {
                    "Name": "$issuer",
                    "Action": "Pay"
                  }
                ]
              }
            }"""
        }
    }

    data class BuckarooTransaction(
            val transaction: PaymentTransaction,
            val redirectUrl: String
    )

    fun createTransaction(paymentMethod: PaymentMethod): BuckarooTransaction {
        val postContent = when (paymentMethod) {
            is PaymentMethod.Ideal -> paymentMethod.getContent()
            is PaymentMethod.CreditCard -> paymentMethod.getContent()
        }
        val nonce = getNonce()
        val httpMethod = HttpMethod.POST.name
        val authHeader = authHeader(requestUri, postContent, httpMethod, nonce)
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", authHeader)
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity(postContent, headers)

        val res = restTemplate.postForObject("https://$requestUri", entity, ObjectNode::class.java)

        val reference = res.get("Key").asText()
        val redirectUrl = res.get("RequiredAction").get("RedirectURL").asText()

        val transaction = paymentTransactionRepository.save(PaymentTransaction(
                amount = paymentMethod.amount,
                nonce = nonce,
                reference = reference,
                status = PaymentTransactionStatus.PENDING
        ))

        return BuckarooTransaction(
                transaction = transaction,
                redirectUrl = redirectUrl
        )

    }

    private fun getEncodedContent(content: String): String {
        val md = MessageDigest.getInstance("MD5")
        val md5 = md.digest(content.toByteArray())
        return getEncoder().encodeToString(md5)
    }

    private fun getHash(
            websiteKey: String,
            secretKey: String,
            httpMethod: String,
            nonce: String,
            timeStamp: String,
            requestUri: String,
            content: String
    ): String? {
        val encodedContent = getEncodedContent(content)
        val rawData = websiteKey + httpMethod + requestUri + timeStamp + nonce + encodedContent

        val sha256HMAC = Mac.getInstance("HmacSHA256")
        val secretkey = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        sha256HMAC.init(secretkey)
        return getEncoder().encodeToString(sha256HMAC.doFinal(rawData.toByteArray()))
    }

    private fun getTimeStamp(): String = (Date().time / 1000).toString()

    private fun getNonce(): String = RandomStringUtils.randomAlphanumeric(32)

    private fun authHeader(
            requestUri: String,
            content: String,
            httpMethod: String,
            nonce: String
    ): String {
        val timeStamp = getTimeStamp()
        val url = URLEncoder.encode(requestUri, "UTF-8").toLowerCase()
        val hash = getHash(
                websiteKey,
                secretKey,
                httpMethod,
                nonce,
                timeStamp,
                url,
                content
        )
        return "hmac $websiteKey:$hash:$nonce:$timeStamp"
    }

}
