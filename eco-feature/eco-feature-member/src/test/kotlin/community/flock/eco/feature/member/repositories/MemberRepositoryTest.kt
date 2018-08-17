package community.flock.eco.feature.member.repositories


import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.MemberConfiguration
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.annotation.PostConstruct

@DataJpaTest
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [MemberConfiguration::class])
@AutoConfigureTestDatabase
open class MemberRepositoryTest {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @PostConstruct
    fun init() {
        memberRepository.deleteAll()

        memberRepository.save(Member(
                firstName = "member1",
                surName = "member1",
                email = "member1@gmail.com",
                status = MemberStatus.ACTIVE
        ))
        memberRepository.save(Member(
                firstName = "member2",
                surName = "member2",
                email = "member2@gmail.com",
                status = MemberStatus.ACTIVE
        ))
        memberRepository.save(Member(
                firstName = "member3",
                surName = "member3",
                email = "member3@gmail.com",
                status = MemberStatus.ACTIVE
        ))
    }

    @Test
    fun testsFindById() {
        val res = memberRepository.findById(100)
        assertEquals(Optional.empty<Member>(), res)
    }

    @Test
    fun testsFindByName() {
        val res = memberRepository.findByName("member3")
        assertEquals(1, res.size)
    }

    @Test
    fun testsFindBySearch() {
        val res = memberRepository.findBySearch("member3")
        assertEquals(1, res.size)
    }

    @Test
    fun testsFindBySearchPage() {
        val page = PageRequest.of(0,10)
        val res = memberRepository.findBySearch("member3", page)
        assertEquals(1, res.totalElements)
    }

    @Test
    @Ignore
    fun testsFindByIds() {
        val res = memberRepository.findByIds(listOf(1, 2))
        assertEquals("member1", res[0].firstName)
        assertEquals("member2", res[1].firstName)
    }

    @Test
    fun testsCreate() {
        val member = Member(
                firstName = "Willem",
                surName = "Veelenturf",
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
        val member1 = Member(
                firstName = "Willem",
                surName = "Veelenturf",
                email = "willem.veelenturf@gmail.com1",
                groups = setOf(group)
        )
        val res1 = memberRepository.save(member1)

        assertEquals("Willem", res1.firstName)
        assertEquals("LEKSTREEK", res1.groups.toList()[0].code)

        val member2 = Member(
                firstName = "Willem",
                surName = "Veelenturf",
                email = "willem.veelenturf@gmail.com2",
                groups = setOf(group)
        )
        val res2 = memberRepository.save(member2)

        assertEquals("Willem", res2.firstName)
        assertEquals("LEKSTREEK", res2.groups.toList()[0].code)

    }

}