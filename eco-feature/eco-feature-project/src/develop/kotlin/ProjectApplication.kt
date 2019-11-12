package community.flock.eco.feature.project

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
@Import(ProjectConfiguration::class)
class ProjectApplication

fun main(args: Array<String>) {
    SpringApplication.run(ProjectApplication::class.java, *args)
}
