package community.flock.eco.feature.member.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.events.*
import community.flock.eco.feature.member.graphql.MemberInput
import community.flock.eco.feature.member.mapper.MemberGraphqlMapper
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.LocalDate
import javax.transaction.Transactional


@Component
class MemberService(
        private val publisher: ApplicationEventPublisher,
        private val memberRepository: MemberRepository,
        private val memberGraphqlMapper: MemberGraphqlMapper) {

    fun findAll(pageable: Pageable): Iterable<Member> = memberRepository
            .findAll(pageable)

    fun findAll(specification: Specification<Member>, pageable: Pageable): Page<Member> = memberRepository
            .findAll(specification, pageable)

    fun count(specification: Specification<Member>): Long = memberRepository
            .count(specification)

    fun findById(id: Long) = memberRepository
            .findById(id)
            .toNullable()

    fun findAllByEmail(email: String) = memberRepository
            .findAllByEmail(email)

    fun findByStatus(status: MemberStatus) = memberRepository
            .findByStatus(status)

    fun create(input: Member): Member = input
            .save()
            .publish { CreateMemberEvent(it) }

    fun update(id: Long, input: Member): Member = findById(id)
            ?.apply {
                if (status == MemberStatus.DELETED)
                    error("Cannot update DELETED member")
                if (status == MemberStatus.MERGED)
                    error("Cannot update MERGED member")
            }
            ?.let {
                input.copy(
                        id = it.id,
                        created = it.created,
                        updated = LocalDate.now()
                )
            }
            ?.save()
            ?.publish { UpdateMemberEvent(it) }
            ?: error("Cannot update member")

    fun delete(id: Long) = findById(id)
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
    fun merge(mergeMemberIds: List<Long>, newMember: Member): Member {
        val mergeMembers = memberRepository.findByIds(mergeMemberIds)
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

    private fun Member.publish(function: (member: Member) -> MemberEvent): Member = apply{
        publisher.publishEvent(function(this))
    }

}


