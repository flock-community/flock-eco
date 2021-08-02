package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.forms.UserGroupForm
import community.flock.eco.feature.user.model.UserGroup
import community.flock.eco.feature.user.repositories.UserGroupRepository
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserGroupService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-groups")
class UserGroupController(
    private val userGroupService: UserGroupService,
    private val userRepository: UserRepository,
    private val userGroupRepository: UserGroupRepository
) {

    @GetMapping()
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAllUserGroups(
        @RequestParam(defaultValue = "", required = false) search: String,
        page: Pageable
    ): ResponseEntity<List<UserGroup>> = userGroupRepository
        .findAllByNameIgnoreCaseContaining(search, page)
        .toResponse()

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findUserGroupById(@PathVariable code: String): ResponseEntity<UserGroup> = userGroupRepository
        .findByCode(code)
        .toResponse()

    @PostMapping()
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun createUserGroup(@RequestBody form: UserGroupForm) = userGroupService
        .create(form)
        .toResponse()

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun updateUserGroup(@RequestBody form: UserGroupForm, @PathVariable code: String) = userGroupService
        .update(code, form)
        .toResponse()

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun deleteUserGroup(@PathVariable code: String) = userGroupService
        .delete(code)
        .toResponse()
}
