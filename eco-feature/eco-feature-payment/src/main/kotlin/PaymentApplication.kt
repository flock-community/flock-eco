package community.flock.eco.feature.payment

import community.flock.eco.feature.payment.data.PaymentLoadData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@SpringBootApplication
@Import(PaymentConfiguration::class, PaymentLoadData::class)
class PaymentApplication {
    @Autowired
    lateinit var paymentLoadData: PaymentLoadData

    @PostConstruct
    fun init() {
        paymentLoadData.load()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(PaymentApplication::class.java, *args)
}
