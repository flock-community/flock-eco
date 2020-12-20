package community.flock.eco.feature.payment.controllers

import community.flock.eco.feature.payment.model.PaymentTransaction
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment/transactions")
class PaymentTransactionController(private val paymentTransactionRepository: PaymentTransactionRepository) {

    @GetMapping
    @PreAuthorize("hasAuthority('PaymentTransactionAuthority.READ')")
    fun findAll(pageable: Pageable): Page<PaymentTransaction> = paymentTransactionRepository.findAll(pageable)
}
