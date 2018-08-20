package community.flock.eco.feature.member.controllers


import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import org.springframework.http.HttpStatus




@RestController
@RequestMapping("/api/members")
class MemberController(private val memberRepository: MemberRepository) {

    @GetMapping
    fun findAll(@RequestParam("s")  search:String?, page:Pageable?): ResponseEntity<List<Member>> {

        if(!search.isNullOrBlank() || page != null){
            val res = memberRepository.findBySearch(search!!, page!!)
            val headers = HttpHeaders()
            headers.set("x-page", page.pageNumber.toString())
            headers.set("x-total", res.totalElements.toString())
            return ResponseEntity(res.content.toList(), headers, HttpStatus.OK)
        }

        val res = memberRepository.findAll().toList()
        return ResponseEntity(res.toList(), HttpStatus.OK)

    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): Optional<Member> {
        return memberRepository.findById(id.toLong())
    }

    @GetMapping("/{ids}")
    fun findByIds(@PathVariable("ids") ids: List<String>): List<Member> {
        return memberRepository.findByIds(ids.map { it.toLong() })
    }

    @PostMapping
    fun create(@RequestBody member: Member): Member {
        return memberRepository.save(member.copy(
                id = 0,
                status = MemberStatus.NEW,
                groups = setOf()
        ))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String, @RequestBody member: Member): Member {
        return memberRepository.save(
                member.copy(id = id.toLong())
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String) {
        memberRepository.deleteById(id.toLong())
    }


}
