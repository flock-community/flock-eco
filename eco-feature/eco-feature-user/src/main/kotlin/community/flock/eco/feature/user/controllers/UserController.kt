package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
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
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun findMe(principal: Principal?): ResponseEntity<User> = principal
            ?.let {
                when (principal) {
                    is OAuth2AuthenticationToken -> principal.principal.attributes["email"].toString()
                    is UsernamePasswordAuthenticationToken -> (principal.principal as UserDetail).username
                    else -> null
                }
            }
            ?.let { username ->
                userRepository.findByReference(username).toResponse()
            }
            ?: ResponseEntity.notFound().build()

    @GetMapping()
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAll(page: Pageable): ResponseEntity<List<User>> = userRepository.findAll(page).toResponse(page)

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findById(@PathVariable id: Long): User = userRepository.findById(id).orElse(null)

    @PostMapping()
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun create(@RequestBody user: User): User = userRepository.save(user.copy(id = 0))

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun update(@RequestBody user: User, @PathVariable id: String): User = userRepository.save(user.copy(id = id.toLong()))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UserAuthority.WRITE')")
    fun update(@PathVariable id: String) = userRepository.deleteById(id.toLong())

}


