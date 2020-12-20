package community.flock.eco.iso.country.services

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.iso.country.model.Country
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class CountryIsoService(
    val objectMapper: ObjectMapper
) {

    val resource = ClassPathResource("country-iso-data.json")
    val data = objectMapper.readTree(resource.inputStream)
        .map {
            Country(
                name = it.get("name").asText(),
                alpha2 = it.get("alpha-2").asText(),
                alpha3 = it.get("alpha-3").asText()
            )
        }

    fun findByAlpha2(code: String): Country? = data
        .find { it.alpha2 == code }

    fun findByAlpha3(code: String): Country? = data
        .find { it.alpha3 == code }

    fun findByCode(code: String): Country? = data
        .find { it.alpha2 == code || it.alpha3 == code }
}
