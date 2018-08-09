package community.flock.eco.feature.payments.service

import community.flock.eco.feature.payments.PaymentConfiguration
import community.flock.eco.feature.payments.services.PaymentBuckarooService
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
open class PaymentBuckarooServiceTest {

    @Autowired
    lateinit var service: PaymentBuckarooService

    @Test
    fun createTransaction() {
        val url = service.createTransaction(10.00, "HAllo", "INGBNL2A")
        System.out.println(url);
    }
}