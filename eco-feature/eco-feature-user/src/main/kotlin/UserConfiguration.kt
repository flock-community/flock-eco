package community.flock.eco.feature.user

import community.flock.eco.feature.user.controllers.UserAuthorityController
import community.flock.eco.feature.user.controllers.UserController
import community.flock.eco.feature.user.controllers.UserGroupController
import community.flock.eco.feature.user.controllers.UserStatusController
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.user.services.UserSecurityService
import community.flock.eco.feature.user.services.UserService
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
@EnableConfigurationProperties(UserProperties::class)
@Import(UserController::class,
        UserGroupController::class,
        UserAuthorityController::class,
        UserStatusController::class,
        UserService::class,
        UserAuthorityService::class,
        UserSecurityService::class)
class UserConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
