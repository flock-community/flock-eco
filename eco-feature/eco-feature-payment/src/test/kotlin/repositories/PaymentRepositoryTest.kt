package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.model.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase
class PaymentRepositoryTest {

    @Autowired
    lateinit var paymentTransactionRepository: PaymentTransactionRepository

    @Autowired
    lateinit var paymentMandateRepository: PaymentMandateRepository

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

        Assert.assertNotNull(mandate.id)
        Assert.assertNotNull(transaction.id)

    }

    @Test
    fun testBetween(){
        val now =  LocalDate.now()
        val startDate =now.withDayOfMonth(1)
        val endDate =now.withDayOfMonth(now.lengthOfMonth())
        val transactions = paymentTransactionRepository.findBetweenDate(startDate, endDate)
                .map { it.mandate }
        assertEquals(34, transactions.size)
    }

}
