package community.flock.eco.feature.member

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableAutoConfiguration
class MemberApplication

fun main(args: Array<String>) {
    SpringApplication.run(MemberApplication::class.java, *args)
}



