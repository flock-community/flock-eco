package community.flock.eco.feature.member

import LanguageIsoConfiguration
import community.flock.eco.core.configurations.GraphqlConfiguration
import community.flock.eco.feature.member.controllers.MemberController
import community.flock.eco.feature.member.controllers.MemberFieldController
import community.flock.eco.feature.member.controllers.MemberGroupController
import community.flock.eco.feature.member.mapper.MemberGraphqlMapper
import community.flock.eco.feature.member.resolvers.MemberQueryResolver
import community.flock.eco.feature.member.services.MemberService
import community.flock.eco.iso.country.CountryIsoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(
    GraphqlConfiguration::class,
    CountryIsoConfiguration::class,
    LanguageIsoConfiguration::class,
    MemberService::class,
    MemberController::class,
    MemberGroupController::class,
    MemberFieldController::class,
    MemberGraphqlMapper::class,
    MemberQueryResolver::class
)
class MemberConfiguration
