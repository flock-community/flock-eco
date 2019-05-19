package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toNullable
import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.*
import java.security.Principal
import org.springframework.security.core.userdetails.User as UserDetail

@RestController
@RequestMapping("/api/users")
class UserController(
        private val userRepository: UserRepository,
        private val userService: UserService) {

    data class UserForm(
            val reference: String,
            val name: String,
            val email: String,
            val authorities: Set<String> = setOf()
    )

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun findMe(principal: Principal?): ResponseEntity<User> = principal
            ?.let { userRepository.findByReference(it.name).toNullable() }
            .toResponse()

    @GetMapping()
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAll(
            @RequestParam(defaultValue = "", required = false) search: String,
            page: Pageable): ResponseEntity<List<User>> = userRepository
            .findAllByNameLikeOrEmailLike("%$search%", "%$search%", page)
            .toResponse(page)

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findById(@PathVariable code: String): ResponseEntity<User> = userService
            .read(code).toResponse()

    @PostMapping()
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun create(@RequestBody user: UserForm): ResponseEntity<User> = userService
            .create(user.toUser()).toResponse()

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun update(@RequestBody user: UserForm, @PathVariable id: String): ResponseEntity<User> = userService
            .update(id, user.toUser()).toResponse()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun update(@PathVariable id: String) = userService
            .delete(id)

    fun UserForm.toUser():User = User(
            name = this.name,
            email = this.email,
            reference = this.reference,
            authorities = this.authorities
    )
}


