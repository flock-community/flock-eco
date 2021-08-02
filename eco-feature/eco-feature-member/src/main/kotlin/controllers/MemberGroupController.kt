package community.flock.eco.feature.member.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member_groups")
class MemberGroupController(private val memberGroupRepository: MemberGroupRepository) {

    @GetMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findAll() = memberGroupRepository.findAll()
        .toList()
        .toResponse()

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findById(@PathVariable("id") id: Long) = memberGroupRepository
        .findById(id)
        .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(@RequestBody memberGroup: MemberGroup) = memberGroupRepository
        .save(memberGroup)
        .toResponse()

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun update(@PathVariable("id") id: Long, @RequestBody memberGroup: MemberGroup) = memberGroupRepository
        .save(memberGroup.copy(id = id))
        .toResponse()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun delete(@PathVariable("id") id: Long) = memberGroupRepository
        .deleteById(id)
        .toResponse()
}
