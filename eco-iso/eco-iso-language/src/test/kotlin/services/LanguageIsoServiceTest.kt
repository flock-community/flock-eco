package community.flock.eco.iso.language.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@Import(LanguageIsoService::class, ObjectMapper::class)
class LanguageIsoServiceTest() {

    @Autowired
    lateinit var languageIsoService: LanguageIsoService

    @Test
    fun `find language by alpha-2 code`() {
        val language = languageIsoService.findByAlpha2("nl")
        assertEquals("Dutch; Flemish", language?.name)
        assertEquals("nl", language?.alpha2)
    }

    @Test
    fun `language not found`() {
        val language = languageIsoService.findByAlpha2("xx")
        assertNull(language)
    }

}
