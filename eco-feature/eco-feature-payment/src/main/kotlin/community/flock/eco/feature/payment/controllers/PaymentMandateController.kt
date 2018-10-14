package community.flock.eco.feature.payment.controllers

import community.flock.eco.feature.payment.model.PaymentMandate
import community.flock.eco.feature.payment.model.PaymentTransaction
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment/mandates")
class PaymentMandateController(private val paymentMandateRepository: PaymentMandateRepository) {

    @GetMapping()
    @PreAuthorize("hasAuthority('PaymentMandateAuthority.READ')")
    fun findAll(pageable: Pageable): Page<PaymentMandate> = paymentMandateRepository.findAll(pageable)

}