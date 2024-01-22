package community.flock.eco.feature.member.services

import community.flock.eco.feature.member.events.UpdateMemberEvent
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.repositories.MemberRepository
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

class MemberServiceTest {
    @Test
    fun updateTest() {
        println("Test")

        val applicationEventPublisher = mockk<ApplicationEventPublisher>()
        val memberRepository = mockk<MemberRepository>()

        val service = MemberService(applicationEventPublisher, memberRepository)

        val uuid = UUID.randomUUID()
        val current =
            Member(
                id = 1L,
                uuid = uuid,
                firstName = "firstName-1",
                surName = "surName-1",
            )
        val updated =
            Member(
                firstName = "firstName-2",
                surName = "surName-2",
            )

        fun mutateField(
            field: String,
            value: String,
        ) {
            val declaredField = Member::class.java.getDeclaredField(field)
            declaredField.isAccessible = true
            declaredField.set(current, value)
        }

        every { memberRepository.findByUuid(uuid) } returns Optional.of(current)
        every { memberRepository.save(any()) } answers {
            mutateField("firstName", this.value.firstName)
            mutateField("surName", this.value.surName)
            this.value
        }
        justRun { applicationEventPublisher.publishEvent(ofType(Object::class)) }

        val res = service.update(uuid, updated)

        assertEquals("firstName-2", res.firstName)
        assertEquals("surName-2", res.surName)

        verify {
            applicationEventPublisher.publishEvent(
                withArg<Any> {
                    val obj = it as UpdateMemberEvent
                    assertEquals("firstName-2", obj.member.firstName)
                    assertEquals("surName-2", obj.member.surName)
                    assertEquals("firstName-1", obj.oldMember.firstName)
                    assertEquals("surName-1", obj.oldMember.surName)
                },
            )
        }
    }
}
