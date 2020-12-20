package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.exceptions.UserCannotRemoveOwnAccount
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserAccountService
import community.flock.eco.feature.user.services.UserService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
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
    fun findMeUser(authentication: Authentication) = userService
        .read(authentication.name)
        .toResponse()

    @GetMapping("/me/accounts")
    @PreAuthorize("isAuthenticated()")
    fun findMeUserAccounts(authentication: Authentication) = userAccountService
        .findUserAccountByUserCode(authentication.name)
        .toList()
        .toResponse()

    @GetMapping
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAllUsers(
        @RequestParam(defaultValue = "", required = false) search: String,
        page: Pageable
    ): ResponseEntity<List<User>> {
        return userRepository
            .findAllByNameIgnoreCaseContainingOrEmailIgnoreCaseContaining(search, search, page)
            .toResponse()
    }

    @PostMapping("search")
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAllUsersByCodes(
        @RequestBody(required = false) codes: Set<String>
    ): ResponseEntity<List<User>> {
        return userRepository.findAllByCodeIn(codes)
            .toList()
            .toResponse()
    }

    @PostMapping
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun createUser(@RequestBody form: UserForm): ResponseEntity<User> = userService
        .create(form)
        .toResponse()

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findUserById(@PathVariable code: String): ResponseEntity<User> = userService
        .read(code)
        .toResponse()

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun updateUser(@PathVariable code: String, @RequestBody form: UserForm): ResponseEntity<User> = userService
        .update(code, form)
        .toResponse()

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun deleteUser(@PathVariable code: String, principal: Principal?): ResponseEntity<Unit> {
        if (principal?.name == code) throw UserCannotRemoveOwnAccount()
        return userService
            .delete(code)
            .toResponse()
    }

    @PutMapping("/{code}/reset-password")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun generateUserResetCodeForUserCode(@PathVariable code: String) = userAccountService
        .generateResetCodeForUserCode(code)
        .let { Unit }
        .toResponse()

    @PutMapping("/reset-password")
    fun generateUserResetCode(@RequestBody form: RequestPasswordReset) = userAccountService
        .generateResetCodeForUserEmail(form.email)
        .let { Unit }
        .toResponse()

    data class RequestPasswordReset(
        val email: String
    )
}
