package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.model.PaymentTransaction
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface PaymentTransactionRepository : PagingAndSortingRepository<PaymentTransaction, Long> {
    fun findByReference(name: String): Optional<PaymentTransaction>
}
