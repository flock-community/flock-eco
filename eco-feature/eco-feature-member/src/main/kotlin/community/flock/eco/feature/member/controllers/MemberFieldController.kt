package community.flock.eco.feature.member.controllers

import community.flock.eco.feature.member.model.MemberField
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member_fields")
class MemberFieldController(
        private val memberFieldRepository: MemberFieldRepository) {

    @GetMapping
    fun findAll(): List<MemberField> {
        return memberFieldRepository.findAll().toList()
    }

    @PostMapping
    fun create(@RequestBody memberField: MemberField): MemberField {
        return memberFieldRepository.save(memberField.copy(
                name = memberField.name
                        .replace(" ", "_")
                        .toLowerCase()
        ))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<MemberField> {

        val memberGroupOptional = memberFieldRepository.findById(id)

        return if (memberGroupOptional.isPresent) {
            ResponseEntity(memberGroupOptional.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody memberField: MemberField): MemberField {
        return memberFieldRepository.save(
                memberField.copy(
                        id = id,
                        name = memberField.name.toLowerCase())
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long) {
        memberFieldRepository.deleteById(id)
    }

}
