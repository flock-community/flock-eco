package community.flock.eco.iso.countries.services

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.iso.countries.model.Country
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class IsoCountryService(
        val objectMapper: ObjectMapper
) {

    val resource = ClassPathResource("data.json")
    val data = objectMapper.readTree(resource.file)
            .map {
                Country(
                        name = it.get("name").asText(),
                        alpha2 = it.get("alpha-2").asText(),
                        alpha3 = it.get("alpha-3").asText()
                )
            }

    fun findByAlpha2(code:String):Country? = data
            .find { it.alpha2 == code }

    fun findByAlpha3(code:String):Country? = data
            .find { it.alpha3 == code }

}
