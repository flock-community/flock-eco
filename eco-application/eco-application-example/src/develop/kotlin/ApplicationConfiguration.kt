package community.flock.eco.application.example

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.data.UserLoadData
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@ComponentScan(basePackages = [
    "community.flock.eco.application.example.controllers"
])
@Import(
        UserConfiguration::class,
        UserLoadData::class)
class ApplicationConfiguration
