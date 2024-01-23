package community.flock.eco.feature.member.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.model.MemberFieldType
import community.flock.eco.feature.member.services.MemberFieldService
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
    private val memberFieldService: MemberFieldService,
) {
    data class MemberFieldForm(
        val name: String,
        val label: String,
        val type: String,
        val options: Set<String>?,
    )

    @GetMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findAll(): List<MemberField> =
        memberFieldService
            .findAll()
            .toList()
            .sortedBy { it.name }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<MemberField> =
        memberFieldService
            .findById(id)
            .toResponse()

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.READ')")
    fun findByName(
        @PathVariable("name") name: String,
    ): ResponseEntity<MemberField> =
        memberFieldService
            .findByName(name)
            .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun create(
        @RequestBody memberFieldForm: MemberFieldForm,
    ): ResponseEntity<MemberField> {
        return MemberField(
            name = memberFieldForm.name.lowercase(),
            label = memberFieldForm.label,
            type = MemberFieldType.valueOf(memberFieldForm.type),
            options =
                memberFieldForm.options
                    ?.toSortedSet()
                    ?: sortedSetOf(),
        )
            .let { memberFieldService.create(it) }
            .toResponse()
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody memberFieldForm: MemberFieldForm,
    ): ResponseEntity<MemberField> =
        memberFieldService
            .findById(id)
            ?.let {
                it.copy(
                    name = memberFieldForm.name.lowercase(),
                    label = memberFieldForm.label,
                    type = MemberFieldType.valueOf(memberFieldForm.type),
                    options =
                        memberFieldForm.options
                            ?.toSortedSet()
                            ?: sortedSetOf(),
                )
            }
            ?.let { memberFieldService.update(id, it) }
            .toResponse()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberFieldAuthority.WRITE')")
    fun delete(
        @PathVariable("id") id: Long,
    ) = memberFieldService.delete(id)
}
