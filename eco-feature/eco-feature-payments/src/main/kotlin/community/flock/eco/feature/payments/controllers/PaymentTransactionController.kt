package community.flock.eco.feature.payments.controllers

import community.flock.eco.feature.payments.model.PaymentTransaction
import community.flock.eco.feature.payments.repositories.PaymentTransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/transactions")
open class PaymentTransactionController(private val paymentTransactionRepository: PaymentTransactionRepository) {

    @GetMapping()
    @PreAuthorize("hasAuthority('TransactionAuthorities.READ')")
    fun findAll(pageable: Pageable): Page<PaymentTransaction> {
        return paymentTransactionRepository.findAll(pageable)
    }

}