package community.flock.eco.feature.members

import community.flock.eco.feature.members.controllers.MemberController
import community.flock.eco.feature.members.controllers.MemberGroupController
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(
        MemberController::class,
        MemberGroupController::class)
class MemberConfiguration