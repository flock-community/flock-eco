package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.model.PaymentMandate
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface PaymentMandateRepository : PagingAndSortingRepository<PaymentMandate, Long> {

    fun findByCode(name: String): Optional<PaymentMandate>
}
