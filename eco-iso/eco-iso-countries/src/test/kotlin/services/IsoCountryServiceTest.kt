package community.flock.eco.iso.countries.services

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.iso.countries.model.Country
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@Import(IsoCountryService::class, ObjectMapper::class)
class IsoCountryServiceTest(){

    @Autowired
    lateinit var isoCountryService:IsoCountryService

    @Test
    fun `find country by alpha-2 code`(){
        val country = isoCountryService.findByAlpha2("NL")
        assertEquals("NL", country?.alpha2)
        assertEquals("NLD", country?.alpha3)
    }

    @Test
    fun `find country by alpha-3 code`(){
        val country = isoCountryService.findByAlpha3("NLD")
        assertEquals("NL", country?.alpha2)
        assertEquals("NLD", country?.alpha3)
    }

    @Test
    fun `country not found`(){
        val country = isoCountryService.findByAlpha2("XX")
        assertNull(country)
    }

}
