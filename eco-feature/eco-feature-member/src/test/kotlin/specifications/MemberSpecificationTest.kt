package community.flock.eco.feature.member.specifications

import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.develop.data.MemberLoadData
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import javax.transaction.Transactional

@SpringBootTest(classes = [MemberConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@AutoConfigureWebClient
@Transactional
@Import(MemberLoadData::class)
class MemberSpecificationTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val memberLoadData: MemberLoadData
) {

    @BeforeEach
    fun init() {
        memberLoadData.load(100)
    }

    @Test
    fun `find member by specification search first-name-11`() {
        val specification = MemberSpecification("first-name-11")
        val res = memberRepository.findAll(specification)
        assertEquals(1, res.size)
        assertEquals("first-name-11", res[0].firstName)
    }

    @Test
    fun `find member by specification search SUR-name-111`() {
        val specification = MemberSpecification("SUR-name-11")
        val res = memberRepository.findAll(specification)
        assertEquals(1, res.size)
        assertEquals("sur-name-11", res[0].surName)
    }

    @Test
    fun `find member by specification search SUR-name`() {
        val specification = MemberSpecification("SUR-name")
        val res = memberRepository.findAll(specification)
        assertEquals(100, res.size)
    }

    @Test
    fun `find member by specification with status ACTIVE`() {
        val specification = MemberSpecification(statuses = setOf(MemberStatus.ACTIVE))
        val res = memberRepository.findAll(specification)
        assertEquals(50, res.size)
    }

    @Test
    fun `find member by specification with status ACTIVE and NEW`() {
        val specification = MemberSpecification(statuses = setOf(MemberStatus.ACTIVE, MemberStatus.NEW))
        val res = memberRepository.findAll(specification)
        assertEquals(100, res.size)
    }

    @Test
    fun `find member by specification with group GROUP_3`() {
        val groups = setOf("GROUP_3")
        val specification = MemberSpecification(
            groups = groups
        )
        val res = memberRepository.findAll(specification)
        assertEquals(100, res.size)
    }

    @Test
    fun `find member by specification with group GROUP_3 and status NEW`() {
        val groups = setOf("GROUP_3")
        val statuses = setOf(MemberStatus.ACTIVE)
        val specification = MemberSpecification(
            groups = groups,
            statuses = statuses
        )
        val res = memberRepository.findAll(specification)
        assertEquals(50, res.size)
    }

    @Test
    fun `find member by specification with group GROUP_2,GROUP_4 and`() {
        val group2 = "GROUP_2"
        val group4 = "GROUP_4"
        val specification = MemberSpecification(
            groups = setOf(group2, group4)
        )
        val res = memberRepository.findAll(specification)
        assertEquals(50, res.size)
        assertEquals(25, res.filter { it.groups.map { it.code }.contains(group2) }.size)
        assertEquals(25, res.filter { it.groups.map { it.code }.contains(group4) }.size)
    }
}
