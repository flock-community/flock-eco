package community.flock.eco.application.example

import community.flock.eco.application.example.config.WebMvcConfig
import community.flock.eco.application.example.configuration.WebSecurityConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@SpringBootApplication(
    exclude =
        arrayOf(
            RepositoryRestMvcAutoConfiguration::class,
            UserDetailsServiceAutoConfiguration::class,
        ),
)
@Import(
    WebMvcConfig::class,
    WebSecurityConfig::class,
    ApplicationConfiguration::class,
)
class Application : SpringBootServletInitializer()

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
