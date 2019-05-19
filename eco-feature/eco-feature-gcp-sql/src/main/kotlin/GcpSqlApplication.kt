package community.flock.eco.feature.gcp.sql

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(GcpSqlConfiguration::class)
class GcpRuntimeconfigApplication

fun main(args: Array<String>) {
    SpringApplication.run(GcpRuntimeconfigApplication::class.java, *args)
}
