package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.UserAccount
import community.flock.eco.feature.user.repositories.UserAccountRepository
import community.flock.eco.feature.user.services.UserAccountService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-accounts")
class UserAccountController(
        private val userAccountRepository: UserAccountRepository,
        private val userAccountService: UserAccountService
) {

    @GetMapping
    @PreAuthorize("hasAuthority('UserAccountAuthority.READ')")
    fun findAll(
            @RequestParam(defaultValue = "", required = false) search: String,
            page: Pageable): ResponseEntity<Iterable<UserAccount>> = userAccountRepository
            .findAll()
            .toResponse()

    @PostMapping("/request-reset")
    fun requestPasswordReset(@RequestBody requestReset: RequestReset) = userAccountService
            .requestPasswordReset(requestReset.email)
            .toResponse()

    @PutMapping("/reset")
    fun resetPasswordWithResetCode(@RequestBody info: ResetInfo) = userAccountService
            .resetPasswordWithResetCode(info.resetCode, info.password)
            .toResponse()

    data class RequestReset(
            val email: String
    )

    data class ResetInfo(
            val resetCode: String,
            val password: String
    )

}
