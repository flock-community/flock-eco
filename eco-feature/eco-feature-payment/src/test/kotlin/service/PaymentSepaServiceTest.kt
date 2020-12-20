package community.flock.eco.feature.payment.service

import community.flock.eco.feature.payment.PaymentConfiguration
import community.flock.eco.feature.payment.model.PaymentBankAccount
import community.flock.eco.feature.payment.model.PaymentFrequency
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import community.flock.eco.feature.payment.services.PaymentSepaService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.Month
import java.util.*
import javax.transaction.Transactional

@SpringBootTest(classes = [PaymentConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
class PaymentSepaServiceTest(
    @Autowired private val service: PaymentSepaService,
    @Autowired private val repository: PaymentMandateRepository
) {

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

        assertTrue(res.isPresent, "Mandate not found")

        res.ifPresent {
            assertEquals(code, it.code)
            assertEquals(10.12, it.amount, 0.0)
            assertEquals(LocalDate.now().month, it.collectionMonth)
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

        assertTrue(res.isPresent, "Mandate not found")

        res.ifPresent {
            assertEquals(code, it.code)
            assertEquals(Month.APRIL, it.collectionMonth)
        }
    }
}
