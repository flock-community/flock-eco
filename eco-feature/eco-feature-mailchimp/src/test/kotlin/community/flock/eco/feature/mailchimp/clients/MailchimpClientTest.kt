package community.flock.eco.feature.mailchimp.clients

import community.flock.eco.feature.mailchimp.model.MailchimpMember
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@SpringBootTest
class MailchimpClientTest {

    @Autowired
    private lateinit var mailchimpClient: MailchimpClient

    @Test
    fun `get member`() {
        val email = "test@test.nl"
        val member = mailchimpClient.getMember(email)
        assertNull(member)
    }

    @Test
    fun `post member`() {
        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"
        val memberPost = mailchimpClient.postMember(MailchimpMember(
                email = email,
                firstName = "Test",
                lastName = "Test"
        ))
        assertNotNull(memberPost)
        assertEquals(email, memberPost?.email)
        assertEquals("Test", memberPost?.firstName)
        assertEquals("Test", memberPost?.lastName)

        val memberGet = mailchimpClient.getMember(email)
        assertNotNull(memberGet)
        assertEquals(email, memberGet?.email)
        assertEquals("Test", memberGet?.firstName)
        assertEquals("Test", memberGet?.firstName)
    }

    @Test
    fun `post member no name`() {
        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"
        val memberPost = mailchimpClient.postMember(MailchimpMember(
                email = email
                ))
        assertNotNull(memberPost)
        assertEquals(email, memberPost?.email)
        assertNull(memberPost?.firstName)
        assertNull(memberPost?.lastName)

        val memberGet = mailchimpClient.getMember(email)
        assertNotNull(memberGet)
        assertEquals(email, memberGet?.email)
        assertNull(memberGet?.firstName)
        assertNull(memberGet?.lastName)
    }


    @Test
    fun `put member`() {

        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"

        val memberPost = mailchimpClient.postMember(MailchimpMember(
                email = email,
                firstName = "Test",
                lastName = "Test"
        ))
        assertNotNull(memberPost)
        assertEquals(email, memberPost?.email)
        assertEquals("Test", memberPost?.firstName)
        assertEquals("Test", memberPost?.lastName)

        val memberPut = mailchimpClient.putMember(MailchimpMember(
                email = email,
                firstName = "Test 2",
                lastName = "Tes 2"
        ))
        assertNotNull(memberPut)
        assertEquals(email, memberPut?.email)
        assertEquals("Test 2", memberPut?.firstName)
        assertEquals("Test 2", memberPut?.firstName)
    }

    @Test
    fun `post segment`() {
        val tag = UUID.randomUUID().toString()
        val memberPost1 = mailchimpClient.postSegment(tag)
        assertNotNull(memberPost1)
        assertEquals(tag, memberPost1)

        val memberPost2 = mailchimpClient.postSegment(tag)
        assertNotNull(memberPost2)
        assertEquals(tag, memberPost2)
    }

    @Test
    fun `put tags`() {

        val tags = (0 .. 3).map {
            val tag = UUID.randomUUID().toString()
            val tagPost = mailchimpClient.postSegment(tag)
            assertNotNull(tagPost)
            assertEquals(tag, tagPost)
             tag
        }

        val uuid = UUID.randomUUID().toString()
        val email = "$uuid@test.nl"

        val memberPost = mailchimpClient.postMember(MailchimpMember(
                email = email,
                firstName = "Test",
                lastName = "Test",
                tags = setOf(tags[0], tags[1])

        ))
        assertNotNull(memberPost)
        assertEquals(2, memberPost?.tags?.size)
        assertEquals(setOf(tags[0], tags[1]), memberPost?.tags)

        val memberPut = mailchimpClient.putMember(memberPost!!.copy(
                firstName = "Test1",
                lastName = "Test1"
        ))

        mailchimpClient.putTags(email, setOf(tags[2], tags[3]), setOf(tags[0], tags[1]))

        val memberGet = mailchimpClient.getMember(email)

        assertNotNull(memberPut)
        assertEquals(2, memberGet?.tags?.size)
        assertEquals("Test1", memberGet?.firstName)
        assertEquals("Test1", memberGet?.lastName)
        assertEquals(setOf(tags[2], tags[3]), memberGet?.tags)
    }
}