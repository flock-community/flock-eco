package community.flock.eco.feature.project

import community.flock.eco.feature.project.controllers.ProjectController
import community.flock.eco.feature.project.services.ProjectRoleService
import community.flock.eco.feature.project.services.ProjectService
import community.flock.eco.feature.user.ProjectProperties
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableJpaRepositories
@EntityScan
@EnableConfigurationProperties(ProjectProperties::class)
@Import(
        ProjectService::class,
        ProjectRoleService::class,
        ProjectController::class)
class ProjectConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
