package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.model.*
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.annotation.PostConstruct

@DataJpaTest
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [MemberConfiguration::class])
@AutoConfigureTestDatabase
class MemberRepositoryTest{

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var memberFieldRepository: MemberFieldRepository

    @PostConstruct
    fun init() {
        memberRepository.deleteAll()

        (1..3).forEach {
            memberRepository.save(Member(
                    firstName = "member$it",
                    surName = "member$it",
                    email = "member$it@gmail.com",
                    status = MemberStatus.ACTIVE
            ))
        }

        memberRepository.save(Member(
                firstName = "joop",
                surName = "joop",
                email = "joop@gmail.com",
                status = MemberStatus.ACTIVE
        ))
    }

    @Test
    fun `find member by id`() {
        val res = memberRepository.findById(100)
        assertEquals(Optional.empty<Member>(), res)
    }

    @Test
    fun testsFindByName() {
        val res = memberRepository.findByName("member3")
        assertEquals(1, res.toList().size)
    }

    @Test
    fun testsFindBySearch() {
        val res = memberRepository.findBySearch("member3")
        assertEquals(1, res.toList().size)
    }

    @Test
    fun testsFindBySearch2Page() {
        val page = PageRequest.of(0, 1)
        val res = memberRepository.findBySearch("jo", page)
        assertEquals("joop", res.content[0].firstName)
    }

    @Test
    fun testsFindBySearchPage() {
        val page = PageRequest.of(0, 10)
        val res = memberRepository.findBySearch("member3", page)
        assertEquals(1, res.totalElements)
    }

    @Test
    @Ignore
    fun testsFindByIds() {
        val res = memberRepository.findByIds(listOf(1, 2))
        assertEquals("member1", res.toList()[0].firstName)
        assertEquals("member2", res.toList()[1].firstName)
    }

    @Test
    fun testsCreate() {
        val member = createMember(
                email = "willem.veelenturf@gmail.com"
        )
        val res = memberRepository.save(member)

        assertEquals("Willem", res.firstName)
    }

    @Test
    fun testsGroup() {

        val group = MemberGroup(
                code = "LEKSTREEK",
                name = "Lekstreek"
        )
        val member1 = createMember(
                groups = setOf(group)
        )
        val res1 = memberRepository.save(member1)

        assertEquals("Willem", res1.firstName)
        assertEquals("LEKSTREEK", res1.groups.toList()[0].code)

        val member2 = createMember(
                email = "willem.veelenturf@gmail.com2",
                groups = setOf(group)
        )
        val res2 = memberRepository.save(member2)

        assertEquals("Willem", res2.firstName)
        assertEquals("LEKSTREEK", res2.groups.toList()[0].code)

    }

    @Test
    fun testsField() {

        val fieldAgreement = MemberField(
                name = "agreement",
                label = "Agreement",
                type = MemberFieldType.TEXT
        )

        val fieldCheckbox = MemberField(
                name = "checkbox",
                label = "Checkbox",
                type = MemberFieldType.TEXT
        )

        memberFieldRepository.save(fieldAgreement)
        memberFieldRepository.save(fieldCheckbox)

        val member1 = createMember(fields = mapOf(fieldAgreement.name to "Test123"))
        val res1 = memberRepository.save(member1)

        assertEquals("Willem", res1.firstName)
        assertEquals("Test123", res1.fields["agreement"])

        val member2 = createMember(
                email = "willem.veelenturf@gmail.com2",
                fields = mapOf(fieldCheckbox.name to "Checked")
        )
        val res2 = memberRepository.save(member2)

        assertEquals("Willem", res2.firstName)
        assertEquals("Checked", res2.fields["checkbox"])

    }

    private fun createMember(
            firstName: String = "Willem",
            surName: String = "Veelenturf",
            email: String = "willem.veelenturf@gmail.com1",
            groups: Set<MemberGroup> = setOf(),
            fields: Map<String, String> = mapOf()
    ): Member = Member(
            firstName = firstName,
            surName = surName,
            email = email,
            groups = groups,
            fields = fields
    )

}