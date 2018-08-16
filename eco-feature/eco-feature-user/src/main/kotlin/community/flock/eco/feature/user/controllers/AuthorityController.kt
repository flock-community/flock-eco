package community.flock.eco.feature.user.controllers

import community.flock.eco.core.authorities.Authority
import community.flock.eco.feature.user.repositories.UserRepository
import org.reflections.Reflections
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/authorities")
open class AuthorityController(private val userRepository: UserRepository) {

    val reflections = Reflections("community.flock.eco")
    val classes = reflections.getSubTypesOf(Authority::class.java)

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    fun findMe(): List<String> {
        return classes
                .filter { it.isEnum }
                .flatMap { it.enumConstants.toList() }
                .map { it.toName() }
    }

}