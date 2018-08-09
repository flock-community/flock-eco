package community.flock.eco.feature.payments.repositories

import community.flock.eco.feature.payments.model.PaymentTransaction
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service


@Service
interface PaymentTransactionRepository : PagingAndSortingRepository<PaymentTransaction, Long> {
    fun findByReference(name: String): PaymentTransaction?
}


