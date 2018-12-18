package community.flock.eco.feature.payment

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(PaymentConfiguration::class)
class PaymentApplication

fun main(args: Array<String>) {
    SpringApplication.run(PaymentApplication::class.java, *args)
}
