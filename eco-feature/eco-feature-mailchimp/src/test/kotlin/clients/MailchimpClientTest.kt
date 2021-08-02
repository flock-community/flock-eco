package community.flock.eco.feature.mailchimp.clients

import community.flock.eco.feature.mailchimp.MailchimpConfiguration
import community.flock.eco.feature.mailchimp.model.MailchimpMember
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(classes = [MailchimpConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@AutoConfigureWebClient
@Transactional
@Disabled
class MailchimpClientTest(
    private val mailchimpClient: MailchimpClient
) {

    private val listId = "1d3504aedc"

    @Test
    fun `get member`() {
        val email = "test@test.nl"
        val member = mailchimpClient.getMember(listId, email)
        assertNull(member)
    }

    @Test
    fun `post member`() {
        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"
        val memberPost = mailchimpClient.postMember(
            listId,
            MailchimpMember(
                id = "1",
                webId = "1",
                email = email,
                fields = mapOf(
                    "FNAME" to "Test",
                    "LNAME" to "Test"
                ),
                language = "nl"
            )
        )
        assertNotNull(memberPost)
        assertEquals(email, memberPost?.email)
        assertEquals("Test", memberPost?.fields?.getValue("FNAME"))
        assertEquals("Test", memberPost?.fields?.getValue("LNAME"))

        val memberGet = mailchimpClient.getMember(listId, email)
        assertNotNull(memberGet)
        assertEquals(email, memberGet?.email)
        assertEquals("Test", memberPost?.fields?.getValue("FNAME"))
        assertEquals("Test", memberPost?.fields?.getValue("LNAME"))
    }

    @Test
    fun `post member no name`() {
        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"
        val memberPost = mailchimpClient.postMember(
            listId,
            MailchimpMember(
                id = "1",
                webId = "1",
                email = email,
                language = "nl"
            )
        )
        assertNotNull(memberPost)
        assertEquals(email, memberPost?.email)
        assertNull(memberPost?.fields?.getValue("FNAME"))
        assertNull(memberPost?.fields?.getValue("LNAME"))

        val memberGet = mailchimpClient.getMember(listId, email)
        assertNotNull(memberGet)
        assertEquals(email, memberGet?.email)
        assertNull(memberPost?.fields?.getValue("FNAME"))
        assertNull(memberPost?.fields?.getValue("LNAME"))
    }

    @Test
    fun `put member`() {

        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"

        val memberPost = mailchimpClient.postMember(
            listId,
            MailchimpMember(
                id = "1",
                webId = "1",
                email = email,
                fields = mapOf(
                    "FNAME" to "Test",
                    "LNAME" to "Test"
                ),
                language = "nl"
            )
        )
        assertNotNull(memberPost)
        assertEquals(email, memberPost?.email)
        assertEquals("Test", memberPost?.fields?.getValue("FNAME"))
        assertEquals("Test", memberPost?.fields?.getValue("LNAME"))

        val memberPut = mailchimpClient.putMember(
            listId,
            MailchimpMember(
                id = "1",
                webId = "1",
                email = email,
                fields = mapOf(
                    "FNAME" to "Test 2",
                    "LNAME" to "Test 2"
                ),
                language = "nl"
            )
        )
        assertNotNull(memberPut)
        assertEquals(email, memberPut?.email)
        assertEquals("Test 2", memberPut?.fields?.getValue("FNAME"))
        assertEquals("Test 2", memberPut?.fields?.getValue("LNAME"))
    }

    @Test
    fun `post segment`() {
        val tag = UUID.randomUUID().toString()
        val memberPost1 = mailchimpClient.postSegment(listId, tag)
        assertNotNull(memberPost1)
        assertEquals(tag, memberPost1)

        val memberPost2 = mailchimpClient.postSegment(listId, tag)
        assertNotNull(memberPost2)
        assertEquals(tag, memberPost2)
    }

    @Test
    fun `put tags`() {

        val tags = (0..3).map {
            val tag = UUID.randomUUID().toString()
            val tagPost = mailchimpClient.postSegment(listId, tag)
            assertNotNull(tagPost)
            assertEquals(tag, tagPost)
            tag
        }

        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"

        val memberPost = mailchimpClient.postMember(
            listId,
            MailchimpMember(
                id = "1",
                webId = "1",
                email = email,
                fields = mapOf(
                    "FNAME" to "Test",
                    "LNAME" to "Test"
                ),
                tags = setOf(tags[0], tags[1]),
                language = "nl"

            )
        )
        assertNotNull(memberPost)
        assertEquals(2, memberPost?.tags?.size)
        assertEquals(setOf(tags[0], tags[1]), memberPost?.tags)

        val memberPut = mailchimpClient.putMember(
            listId,
            memberPost!!.copy(
                fields = mapOf(
                    "FNAME" to "Test1",
                    "LNAME" to "Test1"
                )
            )
        )

        mailchimpClient.putTags(listId, email, setOf(tags[2], tags[3]), setOf(tags[0], tags[1]))

        val memberGet = mailchimpClient.getMember(listId, email)

        assertNotNull(memberPut)
        assertEquals(2, memberGet?.tags?.size)
        assertEquals("Test1", memberGet?.fields?.getValue("FNAME"))
        assertEquals("Test1", memberGet?.fields?.getValue("LNAME"))
        assertEquals(setOf(tags[2], tags[3]), memberGet?.tags)
    }
}
