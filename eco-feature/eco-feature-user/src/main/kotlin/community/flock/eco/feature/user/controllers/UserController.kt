package community.flock.eco.feature.user.controllers

import community.flock.eco.core.services.MailService
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun findMe(principal: Principal): Principal = principal

    @GetMapping("/me/mail")
    @PreAuthorize("isAuthenticated()")
    fun mailMe(mailService: MailService, principal: Principal): String {
        mailService.sendMail(principal)
        return "mail sent"
    }

    @GetMapping("/strategy")
    @PreAuthorize("isAuthenticated()")
    fun findStrategy(): String? = SecurityContextHolder.getContextHolderStrategy().javaClass.name

    @GetMapping()
    @PreAuthorize("hasAuthority('UserAuthority.READ')")
    fun findAll(pageable: Pageable): Page<User> = userRepository.findAll(pageable)

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
