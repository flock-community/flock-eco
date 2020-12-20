package community.flock.eco.iso.country.services

import community.flock.eco.iso.country.CountryIsoConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest(classes = [CountryIsoConfiguration::class])
@AutoConfigureWebClient
class CountryIsoServiceTest(
    @Autowired private val countryIsoService: CountryIsoService
) {

    @Test
    fun `find country by alpha-2 code`() {
        val country = countryIsoService.findByAlpha2("NL")
        assertEquals("NL", country?.alpha2)
        assertEquals("NLD", country?.alpha3)
    }

    @Test
    fun `find country by alpha-3 code`() {
        val country = countryIsoService.findByAlpha3("NLD")
        assertEquals("NL", country?.alpha2)
        assertEquals("NLD", country?.alpha3)
    }

    @Test
    fun `country not found`() {
        val country = countryIsoService.findByAlpha2("XX")
        assertNull(country)
    }
}
