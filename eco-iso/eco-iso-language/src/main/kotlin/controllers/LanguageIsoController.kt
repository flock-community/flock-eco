package community.flock.eco.iso.language.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.iso.language.graphql.Language
import community.flock.eco.iso.language.services.LanguageIsoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/languages")
class LanguageIsoController(
    val languageIsoService: LanguageIsoService
) {

    @GetMapping
    fun getAll() = languageIsoService.data
        .map {
            Language(
                name = it.name,
                alpha2 = it.alpha2
            )
        }
        .toResponse()

    @GetMapping("/{code}")
    fun getById(@PathVariable code: String) = languageIsoService.findByAlpha2(code)
        ?.let {
            Language(
                name = it.name,
                alpha2 = it.alpha2
            )
        }
        .toResponse()
}
