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
@RequestMapping("/api/users/{id}/reset")
class UserSecretResetController(private val userRepository: UserRepository) {

    @PostMapping()
    fun reset(@PathVariable id:String): ResponseEntity<*> = TODO()

}


