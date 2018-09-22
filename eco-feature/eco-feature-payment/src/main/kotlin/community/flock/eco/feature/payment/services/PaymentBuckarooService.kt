package community.flock.eco.feature.payment.services

import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.payment.model.PaymentTransaction
import community.flock.eco.feature.payment.model.PaymentTransactionStatus
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*
import java.util.Base64.getEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class PaymentBuckarooService {

    enum class PaymentMethod {
        IDEAL,
        CREDITCARD
    }

    data class BuckarooTransaction(
            val transaction: PaymentTransaction,
            val redirectUrl: String
    )

    @Value("\${buckaroo.requestUri}")
    private var requestUri: String = ""

    @Value("\${buckaroo.websiteKey}")
    private var websiteKey: String = ""

    @Value("\${buckaroo.secretKey}")
    private var secretKey: String = ""

    @Autowired
    lateinit var paymentTransactionRepository: PaymentTransactionRepository

    fun createTransaction(paymentMethod: PaymentMethod, issuer: String, amount: Double, description: String): BuckarooTransaction {
        val nonce = getNonce()
        val postContent = getContent(paymentMethod, issuer, amount, description);
        val httpMethod = "POST"
        val authHeader = authHeader(requestUri, postContent, httpMethod, nonce)
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", authHeader)
        headers.set("Content-Type", "application/json")

        val entity = HttpEntity(postContent, headers)

        val res = restTemplate.postForObject("https://$requestUri", entity, ObjectNode::class.java)

        val reference = res.get("Key").asText()
        val redirectUrl = res.get("RequiredAction").get("RedirectURL").asText()

        val transaction = paymentTransactionRepository.save(PaymentTransaction(
                amount = amount,
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
        val base64 = getEncoder().encodeToString(md5)
        return base64
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

    private fun getTimeStamp(): String {
        return (Date().time / 1000).toString()
    }

    private fun getNonce(): String {
        return RandomStringUtils.randomAlphanumeric(32)
    }

    private fun getContent(paymentMethod: PaymentMethod, issuer: String, amount: Double, description: String): String {
        if (paymentMethod == PaymentMethod.IDEAL) {
            return """{
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
        if (paymentMethod == PaymentMethod.CREDITCARD) {
            return """{
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

        throw RuntimeException("Payment methode not found")
    }

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