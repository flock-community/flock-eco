import community.flock.eco.core.configurations.GraphqlConfiguration
import community.flock.eco.iso.language.controllers.LanguageIsoController
import community.flock.eco.iso.language.resolvers.LanguageIsoQueryResolver
import community.flock.eco.iso.language.services.LanguageIsoService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    GraphqlConfiguration::class,
    LanguageIsoService::class,
    LanguageIsoController::class,
    LanguageIsoQueryResolver::class
)
class LanguageIsoConfiguration
