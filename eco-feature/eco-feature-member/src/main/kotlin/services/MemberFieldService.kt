package community.flock.eco.feature.member.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.events.*
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class MemberFieldService(
    private val publisher: ApplicationEventPublisher,
    private val memberFieldRepository: MemberFieldRepository
) {

    fun findByName(name: String): MemberField? = memberFieldRepository
        .findByName(name)
        .toNullable()

    fun upsert(input: MemberField): MemberField = findByName(input.name)
        ?.apply {
            memberFieldRepository.save(input.copy(id = id))
        }
        ?: memberFieldRepository.save(input)

    fun delete(name: String) = findByName(name)
        ?.apply {
            memberFieldRepository.deleteById(id)
        }

    private fun Member.publish(function: (member: Member) -> MemberEvent): Member = apply {
        publisher.publishEvent(function(this))
    }
}
