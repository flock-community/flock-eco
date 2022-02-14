package community.flock.eco.feature.member.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.member.events.CreateMemberGroupEvent
import community.flock.eco.feature.member.events.DeleteMemberGroupEvent
import community.flock.eco.feature.member.events.UpdateMemberGroupEvent
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member_groups")
class MemberGroupService(
    private val publisher: ApplicationEventPublisher,
    private val memberGroupRepository: MemberGroupRepository
) {

    fun findAll(): Iterable<MemberGroup> = memberGroupRepository.findAll()

    fun findById(@PathVariable("id") id: Long) = memberGroupRepository
        .findById(id)
        .toNullable()

    fun create(@RequestBody memberGroup: MemberGroup): MemberGroup = memberGroupRepository
        .save(memberGroup)
        .also { publisher.publishEvent(CreateMemberGroupEvent(it)) }

    fun update(id: Long, memberGroup: MemberGroup) = findById(id)
        ?.let {
            val cur = it.copy()
            val res = memberGroupRepository.save(memberGroup.copy(id = it.id))
            publisher.publishEvent(UpdateMemberGroupEvent(res, cur))
        }

    fun delete(@PathVariable("id") id: Long) = findById(id)
        ?.let {
            memberGroupRepository.deleteById(id)
            publisher.publishEvent(DeleteMemberGroupEvent(it))
        }
}
