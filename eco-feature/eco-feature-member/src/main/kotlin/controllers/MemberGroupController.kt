package community.flock.eco.feature.member.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.services.MemberGroupService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member_groups")
class MemberGroupController(private val memberGroupService: MemberGroupService) {

    @GetMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findAll() = memberGroupService.findAll()
        .toList()
        .toResponse()

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findById(@PathVariable("id") id: Long) = memberGroupService
        .findById(id)
        .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(@RequestBody memberGroup: MemberGroup) = memberGroupService
        .create(memberGroup)
        .toResponse()

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun update(@PathVariable("id") id: Long, @RequestBody memberGroup: MemberGroup) = memberGroupService
        .update(id, memberGroup)
        .toResponse()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun delete(@PathVariable("id") id: Long) = memberGroupService
        .delete(id)
        .toResponse()
}
