package community.flock.eco.iso.country.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@Import(CountryIsoService::class, ObjectMapper::class)
class CountryIsoServiceTest(){

    @Autowired
    lateinit var countryIsoService:CountryIsoService

    @Test
    fun `find country by alpha-2 code`(){
        val country = countryIsoService.findByAlpha2("NL")
        assertEquals("NL", country?.alpha2)
        assertEquals("NLD", country?.alpha3)
    }

    @Test
    fun `find country by alpha-3 code`(){
        val country = countryIsoService.findByAlpha3("NLD")
        assertEquals("NL", country?.alpha2)
        assertEquals("NLD", country?.alpha3)
    }

    @Test
    fun `country not found`(){
        val country = countryIsoService.findByAlpha2("XX")
        assertNull(country)
    }

}
