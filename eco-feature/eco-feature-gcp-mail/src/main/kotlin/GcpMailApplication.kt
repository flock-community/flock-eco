package community.flock.eco.feature.gcp.mail

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(GcpMailConfiguration::class)
class GcpMailApplication

fun main(args: Array<String>) {
    SpringApplication.run(GcpMailApplication::class.java, *args)
}
