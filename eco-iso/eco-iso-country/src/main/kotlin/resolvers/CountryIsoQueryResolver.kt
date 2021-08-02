package community.flock.eco.iso.country.resolvers

import community.flock.eco.iso.country.graphql.Country
import community.flock.eco.iso.country.services.CountryIsoService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class CountryIsoQueryResolver(
    private val countryIsoService: CountryIsoService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findCountryAll(): Iterable<Country> = countryIsoService
        .data
        .map {
            Country(
                name = it.name,
                alpha2 = it.alpha2,
                alpha3 = it.alpha3
            )
        }

    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findCountryByCode(code: String): Country? = countryIsoService
        .findByCode(code)
        ?.let {
            Country(
                name = it.name,
                alpha2 = it.alpha2,
                alpha3 = it.alpha3
            )
        }
}
