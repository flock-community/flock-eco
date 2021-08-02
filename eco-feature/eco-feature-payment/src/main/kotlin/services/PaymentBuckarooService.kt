package community.flock.eco.feature.payment.services

import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.payment.exceptions.PaymentNotCreatedException
import community.flock.eco.feature.payment.model.*
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*
import java.util.Base64.getEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class PaymentBuckarooService(
    @Value("\${flock.eco.feature.payment.buckaroo.requestUri:@null}")
    private val requestUri: String,

    @Value("\${flock.eco.feature.payment.buckaroo.websiteKey:@null}")
    private val websiteKey: String,

    @Value("\${flock.eco.feature.payment.buckaroo.secretKey:@null}")
    private val secretKey: String,

    private val paymentMandateRepository: PaymentMandateRepository,
    private val paymentTransactionRepository: PaymentTransactionRepository
) {

    sealed class PaymentMethod {

        abstract val amount: Double
        abstract val successUrl: String?
        abstract val failureUrl: String?

        abstract fun getType(): PaymentType
        abstract fun getContent(): String

        data class Ideal(
            override val amount: Double,
            override val successUrl: String? = null,
            override val failureUrl: String? = null,
            val description: String,
            val issuer: String
        ) : PaymentMethod() {
            override fun getType() = PaymentType.IDEAL
            override fun getContent(): String = """{
                "Currency": "EUR",
                "AmountDebit": $amount,
                "Invoice": "$description",
                "ClientIP": {
                    "Type": 0,
                    "Address": "0.0.0.0"
                },
                ${if (successUrl != null) """
                   "ReturnURL": "$successUrl",
                """ else ""}
                ${if (failureUrl != null) """
                    "ReturnURLCancel": "$failureUrl",
                    "ReturnURLError": "$failureUrl",
                    "ReturnURLReject": "$failureUrl",
                """ else ""}
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
                }
            }"""
        }

        data class CreditCard(
            override val amount: Double,
            override val successUrl: String? = null,
            override val failureUrl: String? = null,
            val description: String,
            val issuer: String
        ) : PaymentMethod() {
            override fun getType() = PaymentType.CREDIT_CARD
            override fun getContent(): String = """{
                "Currency": "EUR",
                "AmountDebit": $amount,
                "Invoice": "$description",
                "ClientIP": {
                    "Type": 0,
                    "Address": "0.0.0.0"
                },
               ${if (successUrl != null) """
                   "ReturnURL": "$successUrl",
                """ else ""}
                ${if (failureUrl != null) """
                    "ReturnURLCancel": "$failureUrl",
                    "ReturnURLError": "$failureUrl",
                    "ReturnURLReject": "$failureUrl",
                """ else ""}
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

    data class BuckarooResult(
        val mandate: PaymentMandate,
        val transaction: PaymentTransaction,
        val redirectUrl: String
    )

    fun create(paymentMethod: PaymentMethod): BuckarooResult {
        val postContent = paymentMethod.getContent()
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            set(
                "Authorization",
                AuthHeader(
                    websiteKey = websiteKey,
                    nonce = RandomStringUtils.randomAlphanumeric(32),
                    secretKey = secretKey,
                    requestUri = requestUri,
                    content = postContent,
                    httpMethod = HttpMethod.POST.name
                ).toString()
            )
            contentType = MediaType.APPLICATION_JSON
        }

        try {
            val res = restTemplate.postForEntity("https://$requestUri", HttpEntity(postContent, headers), ObjectNode::class.java)
            if (res.body != null && res.statusCode.is2xxSuccessful) {
                val mandate = PaymentMandate(
                    code = UUID.randomUUID().toString(),
                    amount = paymentMethod.amount,
                    frequency = PaymentFrequency.ONCE,
                    type = paymentMethod.getType()
                ).also { paymentMandateRepository.save(it) }

                val transaction = PaymentTransaction(
                    amount = paymentMethod.amount,
                    reference = res.body["Key"].asText(),
                    status = PaymentTransactionStatus.PENDING,
                    mandate = mandate
                ).also { paymentTransactionRepository.save(it) }

                if (res.body["Status"]["Code"]["Code"].asInt() in 400..499) {
                    throw PaymentNotCreatedException(res.body["Status"]["SubCode"]["Description"].asText())
                }

                return BuckarooResult(
                    mandate = mandate,
                    transaction = transaction,
                    redirectUrl = res.body["RequiredAction"]["RedirectURL"].asText()
                )
            } else {
                throw PaymentNotCreatedException(res.toString())
            }
        } catch (ex: HttpClientErrorException) {
            throw PaymentNotCreatedException(ex.responseBodyAsString)
        }
    }

    private class AuthHeader(
        private val websiteKey: String,
        private val nonce: String,
        secretKey: String,
        requestUri: String,
        content: String,
        httpMethod: String
    ) {

        private val timeStamp = (Date().time / 1000).toString()
        private val hash = Hash(
            secretKey = secretKey,
            websiteKey = websiteKey,
            httpMethod = httpMethod,
            nonce = nonce,
            timeStamp = timeStamp,
            requestUri = URLEncoder.encode(requestUri, "UTF-8").toLowerCase(),
            content = content
        ).toString()

        override fun toString(): String = "hmac $websiteKey:$hash:$nonce:$timeStamp"
    }

    private class Hash(
        secretKey: String,
        websiteKey: String,
        httpMethod: String,
        nonce: String,
        timeStamp: String,
        requestUri: String,
        content: String
    ) {

        private val secretKey: ByteArray = secretKey.toByteArray()
        private val params: ByteArray = arrayOf(websiteKey, httpMethod, requestUri, timeStamp, nonce, getEncodedContent(content))
            .reduce { acc, cur -> acc + cur }
            .toByteArray()

        override fun toString(): String = getEncoder().encodeToString(
            Mac.getInstance("HmacSHA256")
                .apply { init(SecretKeySpec(secretKey, "HmacSHA256")) }
                .doFinal(params)
        )

        private fun getEncodedContent(content: String): String = getEncoder().encodeToString(
            MessageDigest.getInstance("MD5")
                .digest(content.toByteArray())
        )
    }
}
