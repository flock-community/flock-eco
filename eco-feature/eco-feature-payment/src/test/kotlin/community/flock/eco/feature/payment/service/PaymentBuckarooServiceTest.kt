package community.flock.eco.feature.payment.service

import community.flock.eco.feature.payment.services.PaymentBuckarooService
import community.flock.eco.feature.payment.PaymentConfiguration
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [PaymentConfiguration::class])
@AutoConfigureTestDatabase
class PaymentBuckarooServiceTest {

    @Autowired
    lateinit var service: PaymentBuckarooService

    @Test
    fun createCreditcardTransaction() {
        val url = service.create(PaymentBuckarooService.PaymentMethod.CreditCard(issuer = "visa", amount = 10.00, description = "Creditcard"))
        System.out.println(url)
    }

    @Test
    fun createIdealTransaction() {
        val url = service.create(PaymentBuckarooService.PaymentMethod.Ideal(issuer = "INGBNL2A", amount = 11.00, description = "Ideal"))
        System.out.println(url)
    }
}