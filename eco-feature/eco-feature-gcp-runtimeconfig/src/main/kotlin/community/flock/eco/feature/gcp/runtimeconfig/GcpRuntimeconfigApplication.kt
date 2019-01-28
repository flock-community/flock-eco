package community.flock.eco.feature.gcp.runtimeconfig

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(GcpRuntimeconfigConfiguration::class)
class GcpRuntimeconfigApplication

fun main(args: Array<String>) {
    SpringApplication.run(GcpRuntimeconfigApplication::class.java, *args)
}
