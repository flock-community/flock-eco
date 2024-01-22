package community.flock.eco.feature.member.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.events.CreateMemberFieldEvent
import community.flock.eco.feature.member.events.DeleteMemberFieldEvent
import community.flock.eco.feature.member.events.UpdateMemberFieldEvent
import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class MemberFieldService(
    private val publisher: ApplicationEventPublisher,
    private val memberFieldRepository: MemberFieldRepository,
) {
    fun findById(id: Long): MemberField? =
        memberFieldRepository
            .findById(id)
            .toNullable()

    fun findByName(name: String): MemberField? =
        memberFieldRepository
            .findByName(name)
            .toNullable()

    fun findAll(): Iterable<MemberField> = memberFieldRepository.findAll()

    @Transactional
    fun create(input: MemberField): MemberField =
        memberFieldRepository
            .save(input)
            .also { publisher.publishEvent(CreateMemberFieldEvent(it)) }

    @Transactional
    fun update(
        id: Long,
        input: MemberField,
    ): MemberField? =
        findById(id)
            ?.apply {
                val cur = this.copy()
                val res = memberFieldRepository.save(input.copy(id = id))
                publisher.publishEvent(UpdateMemberFieldEvent(res, cur))
            }

    @Transactional
    fun delete(id: Long) =
        findById(id)
            ?.let {
                memberFieldRepository.deleteById(id)
                publisher.publishEvent(DeleteMemberFieldEvent(it))
            }
}
