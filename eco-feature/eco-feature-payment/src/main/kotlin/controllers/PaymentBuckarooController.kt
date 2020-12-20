package community.flock.eco.feature.payment.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.payment.event.PaymentFailureEvent
import community.flock.eco.feature.payment.event.PaymentSuccessEvent
import community.flock.eco.feature.payment.model.PaymentTransactionStatus
import community.flock.eco.feature.payment.model.PaymentTransactionStatus.*
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
    fun success(@RequestBody json: String): ResponseEntity<Unit> = respond(json, SUCCESS)

    @PostMapping("error")
    fun error(@RequestBody json: String): ResponseEntity<Unit> = respond(json, ERROR)

    private fun respond(json: String, status: PaymentTransactionStatus): ResponseEntity<Unit> = mapper.readValue(json, ObjectNode::class.java)
        .let { it["Transaction"]["Key"].asText() }
        .apply {
            transactionRepository.findByReference(this)?.let {
                transactionRepository.save(
                    it.copy(
                        confirmed = LocalDate.now(),
                        status = status
                    )
                )
            }
        }
        .also { publisher.publishEvent(selectEvent(status)) }
        .let { ResponseEntity(HttpStatus.NO_CONTENT) }

    private fun selectEvent(status: PaymentTransactionStatus) = when (status) {
        SUCCESS -> PaymentSuccessEvent()
        ERROR -> PaymentFailureEvent()
        PENDING -> TODO()
        CANCELED -> TODO()
        FAILED -> TODO()
    }
}
