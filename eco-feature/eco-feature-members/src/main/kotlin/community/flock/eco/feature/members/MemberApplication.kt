package community.flock.eco.feature.members

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import

@SpringBootApplication
@EnableAutoConfiguration
class MemberApplication

fun main(args: Array<String>) {
    SpringApplication.run(MemberApplication::class.java, *args)
}



