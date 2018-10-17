package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import community.flock.eco.feature.member.safeRespond
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
    fun findById(@PathVariable("id") id: Long): ResponseEntity<MemberField> = memberFieldRepository.findById(id).safeRespond()

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(@RequestBody memberField: MemberField): MemberField = memberFieldRepository.save(
            memberField.copy(name = memberField.name.toLoDash())
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

    private fun String.toLoDash() = this.replace(" ", "_").toLowerCase()

}
