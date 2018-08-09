package community.flock.eco.feature.payments.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.feature.payments.repositories.PaymentTransactionRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/api/buckaroo")
open class PaymentBuckarooController(
        private val transactionRepository: PaymentTransactionRepository) {


    var mapper = ObjectMapper()

    @PostMapping("success")
    fun success(@RequestBody json: String): String {

        val obj = mapper.readValue(json, com.fasterxml.jackson.databind.node.ObjectNode::class.java)

        val key = obj.get("PaymentTransaction").get("Key").asText()

        transactionRepository.findByReference(key)?.let {
            transactionRepository.save(it.copy(
                    confirmed = Date()
            ))
        }

        return "OK"
    }

    @PostMapping("error")
    fun error(): String {
        return "OK"
    }


}

