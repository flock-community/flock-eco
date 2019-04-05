package community.flock.eco.feature.payment.service

import community.flock.eco.feature.payment.PaymentConfiguration
import community.flock.eco.feature.payment.model.PaymentBankAccount
import community.flock.eco.feature.payment.model.PaymentFrequency
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import community.flock.eco.feature.payment.services.PaymentSepaService
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.Month
import java.util.*

@RunWith(SpringRunner::class)
@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase
class PaymentSepaServiceTest {

    @Autowired
    lateinit var service: PaymentSepaService

    @Autowired
    lateinit var repository: PaymentMandateRepository

    @Test
    fun createSimple() {

        val code = UUID.randomUUID().toString()

        val sepa = PaymentSepaService.PaymentSepa(
                code = code,
                amount = 10.12,
                frequency = PaymentFrequency.MONTHLY,
                bankAccount = PaymentBankAccount(
                        name = "W.F. Veelenturf",
                        iban = "NL00ABCD0012345678",
                        bic = "BIC123",
                        country = "NL"
                )
        )

        service.create(sepa)

        val res = repository.findByCode(code)

        Assert.assertTrue("Mandate not found", res != null)

        res.ifPresent {
            Assert.assertEquals(code, it.code)
            Assert.assertEquals(10.12, it.amount, 0.0)
            Assert.assertEquals(LocalDate.now().month, it.collectionMonth)
        }

    }

    @Test
    fun createWithCollectionMonth() {

        val code = UUID.randomUUID().toString()

        val sepa = PaymentSepaService.PaymentSepa(
                code = code,
                amount = 10.12,
                frequency = PaymentFrequency.YEARLY,
                collectionMonth = Month.APRIL,
                bankAccount = PaymentBankAccount(
                        name = "W.F. Veelenturf",
                        iban = "NL00ABCD0012345678",
                        bic = "BIC123",
                        country = "NL"
                )
        )

        service.create(sepa)

        val res = repository.findByCode(code)

        Assert.assertTrue("Mandate not found", res != null)

        res.ifPresent {
            Assert.assertEquals(code, it.code)
            Assert.assertEquals(Month.APRIL, it.collectionMonth)
        }

    }


}