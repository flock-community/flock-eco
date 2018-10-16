package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.model.PaymentMandate
import community.flock.eco.feature.payment.model.PaymentTransaction
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
interface PaymentTransactionRepository : PagingAndSortingRepository<PaymentTransaction, Long> {
    fun findByReference(name: String): Optional<PaymentTransaction>
    fun findByMandate(mandate: PaymentMandate): List<PaymentTransaction>

    @Query("SELECT t FROM PaymentTransaction t WHERE t.created BETWEEN ?1 AND ?2")
    fun findBetweenDate(from: LocalDate, to: LocalDate): List<PaymentTransaction>
}
