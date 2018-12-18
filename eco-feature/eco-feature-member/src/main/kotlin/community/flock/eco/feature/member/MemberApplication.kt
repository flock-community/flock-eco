package community.flock.eco.feature.member

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(MemberConfiguration::class)
class MemberApplication

fun main(args: Array<String>) {
    SpringApplication.run(MemberApplication::class.java, *args)
}
