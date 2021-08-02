package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.model.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import javax.transaction.Transactional

@SpringBootTest(classes = [MemberConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@AutoConfigureWebClient
@Transactional
class MemberRepositoryTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val memberFieldRepository: MemberFieldRepository
) {

    @BeforeEach
    fun init() {
        memberRepository.deleteAll()

        (1..3).forEach {
            memberRepository.save(
                Member(
                    firstName = "member$it",
                    surName = "member$it",
                    email = "member$it@gmail.com",
                    status = MemberStatus.ACTIVE
                )
            )
        }

        memberRepository.save(
            Member(
                firstName = "joop",
                surName = "joop",
                email = "joop@gmail.com",
                status = MemberStatus.ACTIVE
            )
        )
    }

    @Test
    fun `find member by id`() {
        val res = memberRepository.findById(100)
        assertEquals(Optional.empty<Member>(), res)
    }

    @Test
    fun testsFindByIds() {
        val members = memberRepository.findAll()
        val res = memberRepository.findByIds(listOf(members.elementAt(0).id, members.elementAt(1).id))
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
            groups = mutableSetOf(group)
        )
        val res1 = memberRepository.save(member1)

        assertEquals("Willem", res1.firstName)
        assertEquals("LEKSTREEK", res1.groups.toList()[0].code)

        val member2 = createMember(
            email = "willem.veelenturf@gmail.com2",
            groups = mutableSetOf(group)
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

        val member1 = createMember(fields = mutableMapOf(fieldAgreement.name to "Test123"))
        val res1 = memberRepository.save(member1)

        assertEquals("Willem", res1.firstName)
        assertEquals("Test123", res1.fields["agreement"])

        val member2 = createMember(
            email = "willem.veelenturf@gmail.com2",
            fields = mutableMapOf(fieldCheckbox.name to "Checked")
        )
        val res2 = memberRepository.save(member2)

        assertEquals("Willem", res2.firstName)
        assertEquals("Checked", res2.fields["checkbox"])
    }

    private fun createMember(
        firstName: String = "Willem",
        surName: String = "Veelenturf",
        email: String = "willem.veelenturf@gmail.com1",
        groups: MutableSet<MemberGroup> = mutableSetOf(),
        fields: MutableMap<String, String> = mutableMapOf()
    ): Member = Member(
        firstName = firstName,
        surName = surName,
        email = email,
        groups = groups,
        fields = fields
    )
}
