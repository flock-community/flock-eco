package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member_fields")
class MemberFieldController(
        private val memberFieldRepository: MemberFieldRepository) {

    @GetMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findAll(): List<MemberField> = memberFieldRepository.findAll().toList()

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<MemberField> {

        val memberGroupOptional = memberFieldRepository.findById(id)

        return if (memberGroupOptional.isPresent) {
            ResponseEntity(memberGroupOptional.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(@RequestBody memberField: MemberField): MemberField = memberFieldRepository.save(
            memberField.copy(name = memberField.name.replace(" ", "_").toLowerCase())
    )

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun update(@PathVariable("id") id: Long, @RequestBody memberField: MemberField): MemberField = memberFieldRepository.save(
            memberField.copy(
                    id = id,
                    name = memberField.name.toLowerCase()
            )
    )

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun delete(@PathVariable("id") id: Long) = memberFieldRepository.deleteById(id)

}
