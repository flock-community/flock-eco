package community.flock.eco.application.multitenant

import community.flock.eco.feature.multitenant.MultiTenantConfiguration
import community.flock.eco.feature.user.UserConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@ComponentScan(
    basePackages = [
        "community.flock.eco.application.multitenant.controllers",
    ],
)
@Import(
    UserConfiguration::class,
    MultiTenantConfiguration::class,
)
class ApplicationConfiguration
