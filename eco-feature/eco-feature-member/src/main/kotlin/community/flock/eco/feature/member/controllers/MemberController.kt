package community.flock.eco.feature.member.controllers


import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/members")
class MemberController(private val memberRepository: MemberRepository) {

    @GetMapping
    fun findAll(@RequestParam("s")  search:String?, page:Pageable): List<Member> {
        if(!search.isNullOrBlank()){
            return memberRepository.findBySearch(search!!, page).content.toList()
        }
        return memberRepository.findAll().toList()
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
