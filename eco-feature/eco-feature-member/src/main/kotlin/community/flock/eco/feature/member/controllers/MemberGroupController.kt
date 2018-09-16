package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member_groups")
class MemberGroupController(private val memberGroupRepository: MemberGroupRepository) {

    @GetMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findAll(): List<MemberGroup> {
        return memberGroupRepository.findAll().toList()
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<MemberGroup> {

        val memberGroupOptional = memberGroupRepository.findById(id)

        return if (memberGroupOptional.isPresent) {
            ResponseEntity(memberGroupOptional.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(@RequestBody memberGroup: MemberGroup): MemberGroup {
        return memberGroupRepository.save(memberGroup)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun update(@PathVariable("id") id: Long, @RequestBody memberGroup: MemberGroup): MemberGroup {
        return memberGroupRepository.save(
                memberGroup.copy(id = id)
        )
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun delete(@PathVariable("id") id: Long) {
        memberGroupRepository.deleteById(id)
    }

}
