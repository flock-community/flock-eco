package community.flock.eco.feature.payment.service

import community.flock.eco.feature.payment.PaymentConfiguration
import community.flock.eco.feature.payment.services.PaymentBuckarooService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest(classes = [PaymentConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
class PaymentBuckarooServiceTest(
    @Autowired private val service: PaymentBuckarooService
) {

    @Test
    fun createCreditcardTransaction() {
        PaymentBuckarooService.PaymentMethod.CreditCard(
            issuer = "visa",
            amount = 10.00,
            description = "Creditcard"
        )
            .let { service.create(it) }
    }

    @Test
    fun createIdealTransaction() {
        PaymentBuckarooService.PaymentMethod.Ideal(
            issuer = "INGBNL2A",
            amount = 11.00,
            description = "Ideal"
        )
            .let { service.create(it) }
    }
}
