package community.flock.eco.iso.country.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.iso.country.graphql.Country
import community.flock.eco.iso.country.services.CountryIsoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/countries")
class CountryIsoController(
    val countryIsoService: CountryIsoService
) {

    @GetMapping
    fun getAll() = countryIsoService.data
        .map {
            Country(
                name = it.name,
                alpha2 = it.alpha2,
                alpha3 = it.alpha3
            )
        }
        .toResponse()

    @GetMapping("/{code}")
    fun getById(@PathVariable code: String) = countryIsoService.findByCode(code)
        ?.let {
            Country(
                name = it.name,
                alpha2 = it.alpha2,
                alpha3 = it.alpha3
            )
        }
        .toResponse()
}
