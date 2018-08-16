package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.PaymentConfiguration
import community.flock.eco.feature.payment.model.PaymentTransaction
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
open class PaymentTransactionRepositoryTest {

    @Autowired
    lateinit var paymentTransactionRepository: PaymentTransactionRepository

    @Test
    fun testsSave() {
        paymentTransactionRepository.save(PaymentTransaction(
                amount = 10.0,
                reference = "1010101010"
        ))

    }


}