package community.flock.eco.feature.member.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.controllers.CreateMemberEvent
import community.flock.eco.feature.member.controllers.DeleteMemberEvent
import community.flock.eco.feature.member.controllers.MergeMemberEvent
import community.flock.eco.feature.member.controllers.UpdateMemberEvent
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component


@Component
class MemberService(
        private val publisher: ApplicationEventPublisher,
        private val memberRepository: MemberRepository,
        private val memberGroupRepository: MemberGroupRepository
) {

    fun findAll() = memberRepository
            .findAll()

    fun findById(id:Long) = memberRepository
            .findById(id)
            .toNullable()

    fun findAllByEmail(email:String) = memberRepository
            .findAllByEmail(email)

    fun findByStatus(status: MemberStatus) = memberRepository
            .findByStatus(status)

    fun create(member: Member): Member = member
            .let { memberRepository.save(it) }
            .also { publisher.publishEvent(CreateMemberEvent(it)) }

    fun update(id: Long, member: Member): Member = member
            .copy(
                    id = id,
                    groups = memberGroupRepository
                            .findAllById(member.groups.map { it.id })
                            .toSet())
            .let { memberRepository.save(it) }
            .also { publisher.publishEvent(UpdateMemberEvent(it)) }

    fun delete(id: Long) : Member? = memberRepository.findById(id)
            .map { member -> member
                        .copy(status = MemberStatus.DELETED)
                        .let { memberRepository.save(it) }
                        .also { publisher.publishEvent(DeleteMemberEvent(it)) }
            }
            .orElse(null)

    fun merge(mergeMemberIds: List<Long>, newMember: Member): Member {
        val mergeMembers = memberRepository.findByIds(mergeMemberIds)
                .map { it.copy(status = MemberStatus.MERGED) }
                .let { memberRepository.saveAll(it) }
        return newMember
                .let { memberRepository.save(it) }
                .also {
                    publisher.publishEvent(MergeMemberEvent(it, mergeMembers.toSet()))
                }
    }

}
