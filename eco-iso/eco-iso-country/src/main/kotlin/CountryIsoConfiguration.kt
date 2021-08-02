package community.flock.eco.iso.country

import community.flock.eco.core.configurations.GraphqlConfiguration
import community.flock.eco.iso.country.controllers.CountryIsoController
import community.flock.eco.iso.country.resolvers.CountryIsoQueryResolver
import community.flock.eco.iso.country.services.CountryIsoService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    GraphqlConfiguration::class,
    CountryIsoService::class,
    CountryIsoController::class,
    CountryIsoQueryResolver::class
)
class CountryIsoConfiguration
