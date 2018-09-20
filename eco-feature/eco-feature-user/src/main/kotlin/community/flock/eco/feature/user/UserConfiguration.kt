package community.flock.eco.feature.user

import community.flock.eco.feature.user.controllers.UserAuthorityController
import community.flock.eco.feature.user.controllers.UserController
import community.flock.eco.feature.user.services.UserAuthorityService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(UserController::class,
        UserAuthorityController::class,
        UserAuthorityService::class)
class UserConfiguration
