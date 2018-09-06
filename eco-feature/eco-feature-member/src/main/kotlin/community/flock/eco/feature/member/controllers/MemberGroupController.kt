package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member_groups")
class MemberGroupController(private val memberGroupRepository: MemberGroupRepository) {

    @GetMapping
    fun findAll(): List<MemberGroup> {
        return memberGroupRepository.findAll().toList()
    }

    @PostMapping
    fun create(@RequestBody memberGroup: MemberGroup): MemberGroup {
        return memberGroupRepository.save(memberGroup)
    }

    @GetMapping("/{code}")
    fun findById(@PathVariable("code") code: Long): ResponseEntity<MemberGroup> {

        val memberGroupOptional = memberGroupRepository.findById(code)

        return if (memberGroupOptional.isPresent) {
            ResponseEntity(memberGroupOptional.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @PutMapping("/{code}")
    fun update(@PathVariable("code") code: String, @RequestBody memberGroup: MemberGroup): MemberGroup {
        return memberGroupRepository.save(
                memberGroup.copy(code = code)
        )
    }

    @DeleteMapping("/{code}")
    fun delete(@PathVariable("code") code: String) {
        memberGroupRepository.deleteByCode(code)
    }

}
