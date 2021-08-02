package community.flock.eco.iso.language.resolvers

import community.flock.eco.iso.language.graphql.Language
import community.flock.eco.iso.language.services.LanguageIsoService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class LanguageIsoQueryResolver(
    val languageIsoService: LanguageIsoService
) : GraphQLQueryResolver {

    fun findLanguageAll(): Iterable<Language> = languageIsoService
        .data
        .map {
            Language(
                name = it.name,
                alpha2 = it.alpha2
            )
        }

    fun findLanguageByCode(code: String): Language? = languageIsoService
        .findByAlpha2(code)
        ?.let {
            Language(
                name = it.name,
                alpha2 = it.alpha2
            )
        }
}
