package community.flock.eco.feature.member

import community.flock.eco.feature.member.controllers.MemberController
import community.flock.eco.feature.member.controllers.MemberFieldController
import community.flock.eco.feature.member.controllers.MemberGroupController
import community.flock.eco.feature.member.resolvers.MemberQueryResolver
import community.flock.eco.feature.member.services.MemberService
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(MemberService::class,
        MemberController::class,
        MemberGroupController::class,
        MemberFieldController::class,
        MemberQueryResolver::class)
class MemberConfiguration : WebMvcConfigurer{
    @Bean
    fun dateType(): GraphQLScalarType? {
        return ExtendedScalars.Date
    }
}
