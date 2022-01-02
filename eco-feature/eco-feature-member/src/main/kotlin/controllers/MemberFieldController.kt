package community.flock.eco.feature.member.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.model.MemberFieldType
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member_fields")
class MemberFieldController(
    private val memberFieldRepository: MemberFieldRepository
) {

    data class MemberFieldForm(
        val name: String,
        val label: String,
        val type: String,
        val options: Set<String>?
    )

    @GetMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findAll(): List<MemberField> = memberFieldRepository.findAll()
        .toList()

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<MemberField> = memberFieldRepository
        .findById(id)
        .toResponse()

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findByName(@PathVariable("name") name: String): ResponseEntity<MemberField> = memberFieldRepository
        .findByName(name)
        .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(@RequestBody memberFieldForm: MemberFieldForm): ResponseEntity<MemberField> {
        return MemberField(
            name = memberFieldForm.name.toLowerCase(),
            label = memberFieldForm.label,
            type = MemberFieldType.valueOf(memberFieldForm.type),
            options = memberFieldForm.options
                ?.toSortedSet()
                ?: sortedSetOf()
        ).let {
            memberFieldRepository.save(it)
        }.let {
            ResponseEntity.ok(it)
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody memberFieldForm: MemberFieldForm
    ): ResponseEntity<MemberField> {

        return memberFieldForm.let {
            memberFieldRepository
                .findById(id)
                .map {
                    it.copy(
                        name = memberFieldForm.name.toLowerCase(),
                        label = memberFieldForm.label,
                        type = MemberFieldType.valueOf(memberFieldForm.type),
                        options = memberFieldForm.options
                            ?.toSortedSet()
                            ?: sortedSetOf()
                    )
                }.map {
                    memberFieldRepository.save(it)
                }.map {
                    ResponseEntity.ok(it)
                }.orElseGet {
                    ResponseEntity
                        .badRequest()
                        .build<MemberField>()
                }
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun delete(@PathVariable("id") id: Long) = memberFieldRepository.deleteById(id)
}
