package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGender
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/members")
class MemberController(
        private val memberRepository: MemberRepository,
        private val memberGroupRepository: MemberGroupRepository
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
            val fields: Map<String, String> = mapOf()
    )


    @GetMapping
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findAll(
            @RequestParam("s") search: String?,
            @RequestParam("status") status: MemberStatus?,
            page: Pageable?): ResponseEntity<List<Member>> {

        val res = memberRepository.findBySearch(
                search = search ?: "",
                status = status?.let { arrayOf(it) } ?: arrayOf(MemberStatus.NEW, MemberStatus.ACTIVE),
                page = page!!)
        val headers = HttpHeaders()
        headers.set("x-page", page.pageNumber.toString())
        headers.set("x-total", res.totalElements.toString())
        return ResponseEntity(res.content.toList(), headers, HttpStatus.OK)

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findById(
            @PathVariable("id") id: String): Optional<Member> = memberRepository
            .findById(id.toLong())

    @PostMapping
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun create(@RequestBody form: MemberForm): Member {
        return form.toMember().let {
            memberRepository.save(it)
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun update(@PathVariable("id") id: String, @RequestBody member: Member): Member = memberRepository.save(member.copy(
            id = id.toLong(),
            groups = memberGroupRepository.findGroups(member)))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun delete(@PathVariable("id") id: String) = memberRepository.findById(id.toLong()).ifPresent { member ->
        member
                .copy(
                        status = MemberStatus.DELETED
                ).let {
                    memberRepository.save(it)
                }
    }

    private fun MemberGroupRepository.findGroups(member: Member): Set<MemberGroup> = this.findAllById(member.groups.map { it.id }).toSet()

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
                fields = this.fields
        )
    }

}
