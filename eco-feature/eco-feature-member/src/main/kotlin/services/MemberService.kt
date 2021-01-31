package community.flock.eco.feature.member.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.events.*
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

@Component
class MemberService(
    private val publisher: ApplicationEventPublisher,
    private val memberRepository: MemberRepository
) {

    fun findAll(pageable: Pageable): Iterable<Member> = memberRepository
        .findAll(pageable)

    fun findAll(specification: Specification<Member>, pageable: Pageable): Page<Member> = memberRepository
        .findAll(specification, pageable)

    fun count(): Long = memberRepository
        .count()

    fun count(specification: Specification<Member>): Long = memberRepository
        .count(specification)

    fun findByUuid(uuid: UUID) = memberRepository
        .findByUuid(uuid)
        .toNullable()

    fun findAllByEmail(email: String) = memberRepository
        .findAllByEmail(email)

    fun findByStatus(status: MemberStatus) = memberRepository
        .findByStatus(status)

    fun create(input: Member): Member = input
        .save()
        .publish { CreateMemberEvent(it) }

    fun update(uuid: UUID, input: Member): Member = findByUuid(uuid)
        ?.apply {
            if (status == MemberStatus.DELETED)
                throw error("Cannot update DELETED member")
            if (status == MemberStatus.MERGED)
                throw error("Cannot update MERGED member")
            val update = input.copy(
                id = id,
                uuid = uuid,
                created = created,
                updated = LocalDate.now()
            )
            update.save()
            update.publish { UpdateMemberEvent(update, this) }
        }
        ?: error("Cannot update member")

    fun delete(uuid: UUID) = findByUuid(uuid)
        ?.let {
            it.copy(
                status = MemberStatus.DELETED,
                updated = LocalDate.now()
            )
        }
        ?.save()
        ?.publish { DeleteMemberEvent(it) }
        ?: error("Cannot delete member")

    @Transactional
    fun merge(mergeMemberIds: List<UUID>, newMember: Member): Member {
        val mergeMembers = memberRepository.findByUuids(mergeMemberIds)
            .map {
                it.copy(
                    status = MemberStatus.MERGED,
                    updated = LocalDate.now()
                )
            }
            .saveAll()
        return newMember
            .save()
            .publish { MergeMemberEvent(it, mergeMembers.toSet()) }
    }

    private fun Member.save() = memberRepository.save(this)
    private fun Iterable<Member>.saveAll() = memberRepository.saveAll(this)

    private fun Member.publish(function: (member: Member) -> MemberEvent): Member = apply {
        publisher.publishEvent(function(this))
    }
}
