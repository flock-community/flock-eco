package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toNullable
import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.UserGroup
import community.flock.eco.feature.user.repositories.UserGroupRepository
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-groups")
class UserGroupController(
        private val userRepository: UserRepository,
        private val userGroupRepository: UserGroupRepository) {

    data class UserGroupForm(
            val name: String,
            val users: Set<String>
    )

    @GetMapping()
    @PreAuthorize("hasAuthority('UserGroupAuthority.READ')")
    fun findAll(@RequestParam(defaultValue = "", required = false) search: String,
                page: Pageable): ResponseEntity<List<UserGroup>> = userGroupRepository
            .findAllByNameLike(search, page)
            .toResponse()

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('UserGroupAuthority.READ')")
    fun findById(@PathVariable code: String): ResponseEntity<UserGroup> = userGroupRepository
            .findByCode(code)
            .toResponse()

    @PostMapping()
    @PreAuthorize("hasAuthority('UserGroupAuthority.WRITE')")
    fun create(@RequestBody form: UserGroupForm): UserGroup = form
            .internalize()
            .let { userGroupRepository.save(it) }

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('UserGroupAuthority.WRITE')")
    fun update(@RequestBody form: UserGroupForm, @PathVariable code: String): ResponseEntity<UserGroup> = userGroupRepository
            .findByCode(code)
            .map { userGroup ->
                form.internalize()
                        .let {
                            it
                        }
                        .copy(
                                code = userGroup.code
                        )
                        .let { userGroupRepository.save(it) }
            }
            .toResponse()

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('UserGroupAuthority.WRITE')")
    fun delete(@PathVariable code: String) = userGroupRepository
            .findByCode(code)
            .toNullable()
            ?.let {
                userGroupRepository.delete(it)
            }
            .toResponse()


    private fun UserGroupForm.internalize(): UserGroup {
        return UserGroup(
                name = this.name,
                users = this.users
                        .mapNotNull { userRepository.findByCode(it).toNullable() }
                        .toSet()

        )
    }

}


