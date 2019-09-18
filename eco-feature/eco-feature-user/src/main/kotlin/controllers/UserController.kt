package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toNullable
import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserAccountService
import community.flock.eco.feature.user.services.UserService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(
        private val userRepository: UserRepository,
        private val userService: UserService,
        private val userAccountService: UserAccountService
) {

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun findMe(principal: Principal?): ResponseEntity<User> = principal
            ?.let { userRepository.findByCode(it.name).toNullable() }
            .toResponse()

    @GetMapping
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAll(
            @RequestParam(defaultValue = "", required = false) search: String,
            page: Pageable): ResponseEntity<List<User>> = userRepository
            .findAllByNameLikeOrEmailLike("%$search%", "%$search%", page)
            .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun create(@RequestBody form: UserForm): ResponseEntity<User> = userService
            .create(form)
            .toResponse()

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findById(@PathVariable code: String): ResponseEntity<User> = userService
            .read(code)
            .toResponse()

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun update(@PathVariable code: String, @RequestBody form: UserForm): ResponseEntity<User> = userService
            .update(code, form)
            .toResponse()

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun update(@PathVariable code: String) = userService
            .delete(code)
            .toResponse()

    @PutMapping("/{code}/reset")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun generateResetCodeForUserCode(@PathVariable code: String) = userAccountService.generateResetCodeForUserCode(code).toResponse()

}


