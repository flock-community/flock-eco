package community.flock.eco.feature.payment.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.core.services.EventService
import community.flock.eco.feature.payment.event.PaymentSuccessEvent
import community.flock.eco.feature.payment.model.PaymentTransactionStatus
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/payment/buckaroo")
class PaymentBuckarooController(
        private val transactionRepository: PaymentTransactionRepository,
        private val eventService: EventService
) {

    val mapper = ObjectMapper()

    @PostMapping("success")
    fun success(@RequestBody json: String): ResponseEntity<Unit> {

        val obj = mapper.readValue(json, ObjectNode::class.java)

        val key = obj["Transaction"]["Key"].asText()

        transactionRepository.findByReference(key).ifPresent {
            transactionRepository.save(it.copy(
                    confirmed = Date(),
                    status = PaymentTransactionStatus.SUCCESS
            ))
        }

        eventService.emitEvent(PaymentSuccessEvent())

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("error")
    fun error(@RequestBody json: String): ResponseEntity<Void> {
        val obj = mapper.readValue(json, ObjectNode::class.java)

        val key = obj["Transaction"]["Key"].asText()

        transactionRepository.findByReference(key).ifPresent {
            transactionRepository.save(it.copy(
                    confirmed = Date(),
                    status = PaymentTransactionStatus.ERROR
            ))
        }

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}

