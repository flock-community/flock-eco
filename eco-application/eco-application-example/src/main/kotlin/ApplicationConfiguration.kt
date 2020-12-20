package community.flock.eco.application.example

import LanguageIsoConfiguration
import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.workspace.WorkspaceConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@ComponentScan
@Import(
    UserConfiguration::class,
    MemberConfiguration::class,
    LanguageIsoConfiguration::class,
    WorkspaceConfiguration::class
)
class ApplicationConfiguration
