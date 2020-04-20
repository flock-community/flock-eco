package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.UserAccount
import community.flock.eco.feature.user.repositories.UserAccountRepository
import community.flock.eco.feature.user.services.UserAccountService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-accounts")
class UserAccountController(
        private val userAccountRepository: UserAccountRepository,
        private val userAccountService: UserAccountService
) {

    @GetMapping
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAll(
            @RequestParam(defaultValue = "", required = false) search: String,
            page: Pageable): ResponseEntity<Iterable<UserAccount>> = userAccountRepository
            .findAll()
            .toResponse()

    @PutMapping("/reset-password")
    fun resetPasswordWithResetCode(@RequestBody form: PasswordResetForm) = userAccountService
            .resetPasswordWithResetCode(form.resetCode, form.password)
            .toResponse()

    @PostMapping("/generate-key")
    @PreAuthorize("isAuthenticated()")
    fun generateKey(authentication: Authentication) = userAccountService
            .generateKeyForUserCode(authentication.name)
            .toResponse()

    @PostMapping("/revoke-key")
    @PreAuthorize("isAuthenticated()")
    fun generateKey(authentication: Authentication, @RequestBody form: KeyRevokeForm) = userAccountService
            .revokeKeyForUserCode(authentication.name, form.key)
            .toResponse()

    data class PasswordResetForm(
            val resetCode: String,
            val password: String
    )

    data class KeyRevokeForm(
            val key: String
    )

}
