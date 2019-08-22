package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserAccount
import community.flock.eco.feature.user.repositories.UserAccountRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-accounts")
class UserAccountController(
        private val userAccountRepository: UserAccountRepository) {

    @GetMapping()
    @PreAuthorize("hasAuthority('UserAccountAuthority.READ')")
    fun findAll(
            @RequestParam(defaultValue = "", required = false) search: String,
            page: Pageable): ResponseEntity<Iterable<UserAccount>> = userAccountRepository
            .findAll()
            .toResponse()



}


