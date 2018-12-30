package community.flock.eco.feature.member.specifications

import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase
class MemberSpecificationTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var memberGroupRepository: MemberGroupRepository

    @Test
    fun `find member by specification search first-name-0`() {
        val specification = MemberSpecification("first-name-0")
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(1, res.size)
        Assert.assertEquals("first-name-0", res[0].firstName)
    }

    @Test
    fun `find member by specification search SUR-name-0`() {
        val specification = MemberSpecification("SUR-name-0")
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(1, res.size)
        Assert.assertEquals("sur-name-0", res[0].surName)
    }

    @Test
    fun `find member by specification search SUR-name`() {
        val specification = MemberSpecification("SUR-name")
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(1000, res.size)
    }

    @Test
    fun `find member by specification with status ACTIVE`() {
        val specification = MemberSpecification(statuses = setOf(MemberStatus.ACTIVE))
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(500, res.size)
    }

    @Test
    fun `find member by specification with status ACTIVE and NEW`() {
        val specification = MemberSpecification(statuses = setOf(MemberStatus.ACTIVE, MemberStatus.NEW))
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(1000, res.size)
    }

    @Test
    fun `find member by specification with group GROUP_3`() {
        val groups = memberGroupRepository.findByCode("GROUP_3")
                .map { setOf(it) }
                .orElseGet { setOf() }
        val specification = MemberSpecification(
                groups = groups)
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(1000, res.size)
    }

    @Test
    fun `find member by specification with group GROUP_3 and status NEW`() {
        val groups = memberGroupRepository.findByCode("GROUP_3")
                .map { setOf(it) }
                .orElseGet { setOf() }
        val statuses = setOf(MemberStatus.ACTIVE)
        val specification = MemberSpecification(
                groups = groups,
                statuses = statuses)
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(500, res.size)
    }

    @Test
    fun `find member by specification with group GROUP_2,GROUP_4 and`() {
        val group2 = memberGroupRepository.findByCode("GROUP_2").get()
        val group4 = memberGroupRepository.findByCode("GROUP_4").get()
        val specification = MemberSpecification(
                groups = setOf(group2,group4))
        val res = memberRepository.findAll(specification)
        Assert.assertEquals(500, res.size)
        Assert.assertEquals(250, res.filter { it.groups.contains(group2) }.size)
        Assert.assertEquals(250, res.filter { it.groups.contains(group4) }.size)
    }
}