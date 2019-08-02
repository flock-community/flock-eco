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
class UserRegisterController(
        private val userRepository: UserRepository,
        private val userService: UserService) {

    data class UserRegisterForm(
            val name: String?,
            val email: String,
            val password: String
    )

    @PostMapping("/register")
    fun register(@RequestBody user: UserRegisterForm): ResponseEntity<User> = userService
            .create(user.toUser())
            .toResponse()

    fun UserRegisterForm.toUser():User = User(
            name = this.name?: "",
            email = this.email,
            reference = this.email,
            secret = this.password
    )
}


