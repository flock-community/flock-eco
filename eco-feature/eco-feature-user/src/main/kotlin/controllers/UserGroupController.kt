package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.UserGroup
import community.flock.eco.feature.user.repositories.UserGroupRepository
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.userdetails.User as UserDetail

@RestController
@RequestMapping("/api/user_groups")
class UserGroupController(
        private val userRepository: UserRepository,
        private val userGroupRepository: UserGroupRepository) {

    data class UserGroupForm(
            val name: String,
            val users: Set<String>
    )

    @GetMapping()
    @PreAuthorize("hasAuthority('UserGroupAuthority.READ')")
    fun findAll(page: Pageable): ResponseEntity<List<UserGroup>> = userGroupRepository
            .findAll(page)
            .toResponse(page)

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('UserGroupAuthority.READ')")
    fun findById(@PathVariable code: String): ResponseEntity<UserGroup> = userGroupRepository
            .findByCode(code)
            .toResponse()

    @PostMapping()
    @PreAuthorize("hasAuthority('UserGroupAuthority.WRITE')")
    fun create(@RequestBody form: UserGroupForm): UserGroup = form
            .let { mapForm(it) }
            .copy(id = 0)
            .let { userGroupRepository.save(it) }

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('UserGroupAuthority.WRITE')")
    fun update(@RequestBody form: UserGroupForm, @PathVariable code: String): ResponseEntity<UserGroup> = userGroupRepository
            .findByCode(code)
            .map { userGroup ->
                form
                        .let { mapForm(it) }
                        .let {
                            it
                        }
                        .copy(
                                id = userGroup.id,
                                code = userGroup.code
                        )
                        .let { userGroupRepository.save(it) }
            }
            .toResponse()

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('UserGroupAuthority.WRITE')")
    fun delete(@PathVariable code: String) = userGroupRepository
            .findByCode(code)
            .map {
                userGroupRepository.delete(it)
            }
            .toResponse()


    private fun mapForm(form: UserGroupForm): UserGroup {
        return UserGroup(
                name = form.name,
                users = form.users
                        .map { userRepository.findByCode(it).orElse(null) }
                        .filter { it != null }
                        .toSet()

        )
    }

}


