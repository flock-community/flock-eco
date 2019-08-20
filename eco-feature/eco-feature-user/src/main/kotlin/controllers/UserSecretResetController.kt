package community.flock.eco.feature.user.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.userdetails.User as UserDetail

@RestController
@RequestMapping("/api/users/{code}/reset")
class UserSecretResetController(private val userService: UserService) {

    @PostMapping()
    fun reset(@PathVariable code: String): ResponseEntity<User> = userService
            .read(code)
            .toResponse()
//            ?.let {
//                userService.resetSecret(it)
//            }
//            .toResponse()

}


