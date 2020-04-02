package community.flock.eco.feature.member.controllers

import community.flock.eco.core.events.Event
import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGender
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.services.MemberService
import community.flock.eco.feature.member.specifications.MemberSpecification
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


sealed class MemberEvent(open val member: Member) : Event
data class CreateMemberEvent(override val member: Member) : MemberEvent(member)
data class UpdateMemberEvent(override val member: Member) : MemberEvent(member)
data class DeleteMemberEvent(override val member: Member) : MemberEvent(member)
data class MergeMemberEvent(override val member: Member, val mergeMembers: Set<Member>) : MemberEvent(member)

@RestController
@RequestMapping("/api/members")
class MemberController(
        private val memberGroupRepository: MemberGroupRepository,
        private val memberService: MemberService
) {

    data class MemberForm(

            val firstName: String,
            val infix: String? = null,
            val surName: String,

            val email: String? = null,

            val phoneNumber: String? = null,

            val street: String? = null,
            val houseNumber: String? = null,
            val houseNumberExtension: String? = null,
            val postalCode: String? = null,
            val city: String? = null,
            val country: String? = null,

            val gender: MemberGender? = null,
            val birthDate: LocalDate? = null,

            val groups: Set<MemberGroup> = setOf(),
            val fields: Map<String, String> = mapOf(),

            val status: MemberStatus = MemberStatus.NEW
    )

    data class MergeForm(val mergeMemberIds: List<Long>, val newMember: MemberForm)

    @GetMapping
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findAll(
            @RequestParam search: String?,
            @RequestParam statuses: Set<MemberStatus>?,
            @RequestParam groups: Set<String>?,
            page: Pageable): ResponseEntity<List<Member>> {

        val specification = MemberSpecification(
                search = search ?: "",
                statuses = statuses ?: setOf(
                        MemberStatus.NEW,
                        MemberStatus.ACTIVE,
                        MemberStatus.DISABLED),
                groups = groups?.let { memberGroupRepository.findByCodes(it).toSet() } ?: setOf()
        )
        return memberService.findAll(specification, page)
                .toResponse()
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findById(
            @PathVariable("id") id: Long) = memberService
            .findById(id)

    @PostMapping
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun create(@RequestBody form: MemberForm): Member = memberService.create(form.toMember())

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun update(@PathVariable("id") id: String, @RequestBody member: Member): Member = memberService.update(id.toLong(), member)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun delete(@PathVariable("id") id: String) = memberService.delete(id.toLong())

    @PostMapping("/merge")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun merge(@RequestBody form: MergeForm): Member = memberService.merge(
            form.mergeMemberIds,
            form.newMember.toMember())

    private fun MemberForm.toMember(): Member {
        return Member(
                firstName = this.firstName,
                infix = this.infix,
                surName = this.surName,
                email = this.email,
                phoneNumber = this.phoneNumber,
                street = this.street,
                houseNumber = this.houseNumber,
                houseNumberExtension = this.houseNumberExtension,
                postalCode = this.postalCode,
                city = this.postalCode,
                country = this.country,
                gender = this.gender,
                groups = this.groups,
                fields = this.fields,
                birthDate = this.birthDate,
                status = this.status
        )
    }

}
