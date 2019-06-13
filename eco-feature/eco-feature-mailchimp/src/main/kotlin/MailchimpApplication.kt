package community.flock.eco.feature.mailchimp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(MailchimpConfiguration::class)
class MailchimpApplication

fun main(args: Array<String>) {
    SpringApplication.run(MailchimpApplication::class.java, *args)
}
