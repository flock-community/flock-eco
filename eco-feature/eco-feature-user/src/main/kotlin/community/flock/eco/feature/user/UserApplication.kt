package community.flock.eco.feature.user

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableAutoConfiguration
class UserApplication

fun main(args: Array<String>) {
    SpringApplication.run(UserApplication::class.java, *args)
}



