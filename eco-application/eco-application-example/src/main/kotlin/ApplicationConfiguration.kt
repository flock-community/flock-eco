package community.flock.eco.application.example

import LanguageIsoConfiguration
import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.develop.data.MemberLoadData
import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.develop.data.UserLoadData
import community.flock.eco.iso.country.CountryIsoConfiguration
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
@Import(UserConfiguration::class,
        UserLoadData::class,
        MemberConfiguration::class,
        MemberLoadData::class,
        CountryIsoConfiguration::class,
        LanguageIsoConfiguration::class)
class ApplicationConfiguration
