package community.flock.eco.iso.language.services

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.iso.language.model.Language
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class LanguageIsoService(
    objectMapper: ObjectMapper
) {

    val resource = ClassPathResource("language-iso-data.json")
    val data = objectMapper.readTree(resource.inputStream)
        .map {
            Language(
                name = it.get("name").asText(),
                alpha2 = it.get("alpha2").asText()
            )
        }

    fun findByAlpha2(code: String): Language? = data
        .find { it.alpha2 == code }
}
