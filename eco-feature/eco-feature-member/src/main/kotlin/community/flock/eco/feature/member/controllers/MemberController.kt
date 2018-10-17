package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.Member
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
import java.util.*

@RestController
@RequestMapping("/api/members")
class MemberController(
        private val memberRepository: MemberRepository,
        private val memberGroupRepository: MemberGroupRepository
) {

    @GetMapping
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findAll(@RequestParam("s") search: String = "", page: Pageable?): ResponseEntity<List<Member>> {

        val res = memberRepository.findBySearch(search, page!!)
        val headers = HttpHeaders()
        headers.set("x-page", page.pageNumber.toString())
        headers.set("x-total", res.totalElements.toString())
        return ResponseEntity(res.content.toList(), headers, HttpStatus.OK)

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findById(@PathVariable("id") id: String): Optional<Member> = memberRepository.findById(id.toLong())

    @GetMapping("/{ids}")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findByIds(@PathVariable("ids") ids: List<String>): List<Member> = memberRepository.findByIds(ids.map { it.toLong() })

    @PostMapping
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun create(@RequestBody member: Member): Member = memberRepository.save(member.copy(
            id = 0,
            status = MemberStatus.NEW,
            groups = memberGroupRepository.findGroups(member)))

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun update(@PathVariable("id") id: String, @RequestBody member: Member): Member = memberRepository.save(member.copy(
            id = id.toLong(),
            groups = memberGroupRepository.findGroups(member)))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MemberAuthority.WRITE')")
    fun delete(@PathVariable("id") id: String) = memberRepository.deleteById(id.toLong())

    private fun MemberGroupRepository.findGroups(member: Member): Set<MemberGroup> = this.findAllById(member.groups.map { it.id }).toSet()

}
