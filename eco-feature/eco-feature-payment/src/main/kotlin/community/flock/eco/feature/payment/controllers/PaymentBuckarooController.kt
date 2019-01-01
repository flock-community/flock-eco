package community.flock.eco.feature.payment.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.payment.event.PaymentFailureEvent
import community.flock.eco.feature.payment.event.PaymentSuccessEvent
import community.flock.eco.feature.payment.model.PaymentTransactionStatus
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/payment/buckaroo")
class PaymentBuckarooController(
        private val transactionRepository: PaymentTransactionRepository,
        private val publisher: ApplicationEventPublisher
) {

    private val mapper = ObjectMapper()

    @PostMapping("success")
    fun success(@RequestBody json: String): ResponseEntity<Unit> {

        val obj = mapper.readValue(json, ObjectNode::class.java)

        val key = obj["Transaction"]["Key"].asText()

        transactionRepository.findByReference(key)?.let {
            transactionRepository.save(it.copy(
                    confirmed = LocalDate.now(),
                    status = PaymentTransactionStatus.SUCCESS
            ))
        }

        publisher.publishEvent(PaymentSuccessEvent())

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("error")
    fun error(@RequestBody json: String): ResponseEntity<Void> {
        val obj = mapper.readValue(json, ObjectNode::class.java)

        val key = obj["Transaction"]["Key"].asText()

        transactionRepository.findByReference(key)?.let {
            transactionRepository.save(it.copy(
                    confirmed = LocalDate.now(),
                    status = PaymentTransactionStatus.ERROR
            ))
        }

        publisher.publishEvent(PaymentFailureEvent())

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}

