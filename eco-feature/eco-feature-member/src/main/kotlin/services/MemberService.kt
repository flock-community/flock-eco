package community.flock.eco.feature.member.services

import MemberGraphqlMapper
import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.events.*
import community.flock.eco.feature.member.graphql.MemberInput
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.transaction.Transactional


@Component
class MemberService(
        private val publisher: ApplicationEventPublisher,
        private val memberRepository: MemberRepository,
        private val memberGraphqlMapper: MemberGraphqlMapper) {

    fun findAll(specification: Specification<Member>, pageable: Pageable): Page<Member> = memberRepository
            .findAll(specification, pageable)

    fun findAll(pageable: Pageable): Iterable<Member> = memberRepository
            .findAll(pageable)

    fun count(specification: Specification<Member>): Long = memberRepository
            .count(specification)

    fun findById(id: Long) = memberRepository
            .findById(id)
            .toNullable()

    fun findAllByEmail(email: String) = memberRepository
            .findAllByEmail(email)

    fun findByStatus(status: MemberStatus) = memberRepository
            .findByStatus(status)

    fun create(input: MemberInput): Member = input
            .consume()
            .save()
            ?.publish { CreateMemberEvent(it) }
            ?: error("Cannot create member")

    fun update(id: Long, input: MemberInput): Member = findById(id)
            ?.run { input.consume(this) }
            ?.save()
            ?.publish { UpdateMemberEvent(it) }
            ?: error("Cannot update member")

    fun delete(id: Long) = findById(id)
            ?.let { it.copy(status = MemberStatus.DELETED) }
            ?.save()
            ?.publish { DeleteMemberEvent(it) }
            ?: error("Cannot delete member")

    @Transactional
    fun merge(mergeMemberIds: List<Long>, newMember: MemberInput): Member {
        val mergeMembers = memberRepository.findByIds(mergeMemberIds)
                .map { it.copy(status = MemberStatus.MERGED) }
                .saveAll()
        return newMember
                .consume()
                .save()
                .publish { MergeMemberEvent(it, mergeMembers.toSet()) }
    }

    private fun Member.save() = memberRepository.save(this)
    private fun Iterable<Member>.saveAll() = memberRepository.saveAll(this)

    private fun Member.publish(function: (member: Member) -> MemberEvent): Member = this
            .also { publisher.publishEvent(it) }

    private fun MemberInput.consume(member: Member? = null) = memberGraphqlMapper
            .consume(this, member)

}


