package community.flock.eco.iso.languages.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@Import(IsoLanguageService::class, ObjectMapper::class)
class IsoLanguageServiceTest() {

    @Autowired
    lateinit var isoLanguageService: IsoLanguageService

    @Test
    fun `find language by alpha-2 code`() {
        val language = isoLanguageService.findByAlpha2("nl")
        assertEquals("Dutch; Flemish", language?.name)
        assertEquals("nl", language?.alpha2)
    }

    @Test
    fun `languages not found`() {
        val language = isoLanguageService.findByAlpha2("xx")
        assertNull(language)
    }

}
