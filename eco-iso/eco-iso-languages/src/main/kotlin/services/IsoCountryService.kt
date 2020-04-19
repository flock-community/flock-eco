package community.flock.eco.iso.languages.services

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.iso.languages.model.Language
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class IsoLanguageService(
        val objectMapper: ObjectMapper
) {

    val resource = ClassPathResource("data.json")
    val data = objectMapper.readTree(resource.file)
            .map {
                Language(
                        name = it.get("name").asText(),
                        alpha2 = it.get("alpha2").asText()
                )
            }

    fun findByAlpha2(code: String): Language? = data
            .find { it.alpha2 == code }

}
