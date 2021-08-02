package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.PaymentConfiguration
import community.flock.eco.feature.payment.model.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import javax.transaction.Transactional
import kotlin.test.assertEquals

@SpringBootTest(classes = [PaymentConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
class PaymentRepositoryTest(
    @Autowired private val paymentTransactionRepository: PaymentTransactionRepository,
    @Autowired private val paymentMandateRepository: PaymentMandateRepository
) {

    @Test
    fun testsSave() {
        val amount = 10.0

        val mandate = PaymentMandate(
            amount = amount,
            frequency = PaymentFrequency.ONCE,
            type = PaymentType.CREDIT_CARD
        ).let {
            paymentMandateRepository.save(it)
        }

        val transaction = PaymentTransaction(
            amount = amount,
            reference = "1010101010",
            status = PaymentTransactionStatus.PENDING,
            mandate = mandate
        ).let {
            paymentTransactionRepository.save(it)
        }

        assertNotNull(mandate.id)
        assertNotNull(transaction.id)

        val now = LocalDate.now()
        val startDate = now.withDayOfMonth(1)
        val endDate = now.withDayOfMonth(now.lengthOfMonth())

        val transactions = paymentTransactionRepository.findBetweenDate(startDate, endDate)
            .map { it.mandate }
        assertEquals(1, transactions.size)
    }
}
