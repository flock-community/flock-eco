package community.flock.eco.feature.user.controllers

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/login")
class UserConfigurationConrtoller {


    data class Configuration(
            val isLoggedIn: Boolean,
            val authorities: List<String>
    )


    @GetMapping("/status")
    fun index(principal: Principal?): Configuration {
        return Configuration(
                isLoggedIn = principal != null,
                authorities = when (principal) {
                    is AbstractAuthenticationToken -> principal.authorities
                            .map { it.authority }
                    else -> listOf()
                }
        )
    }
}