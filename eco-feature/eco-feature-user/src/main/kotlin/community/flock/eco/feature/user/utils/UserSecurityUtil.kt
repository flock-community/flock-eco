package community.flock.eco.feature.user.utils

import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserAuthorityService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority

class UserSecurityUtil(
        val userAuthorityService: UserAuthorityService,
        val userRepository: UserRepository
) {

    fun localLogin(http: HttpSecurity): HttpSecurity {

        val allAuthorities = userAuthorityService
                .allAuthorities()
                .map { it.toName() }
                .map { SimpleGrantedAuthority(it) }
                .plus(SimpleGrantedAuthority("ROLE_USER"))
                .toTypedArray()

        val user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .authorities(*allAuthorities)
                .build()

        http.userDetailsService { user }
        http.formLogin()

        return http
    }

    fun googleLogin(http: HttpSecurity): HttpSecurity {
        http
                .oauth2Login()
                .userInfoEndpoint()
                .userAuthoritiesMapper {
                    val authority = it.first() as OAuth2UserAuthority
                    val name = authority.attributes.get("name").toString()
                    val email = authority.attributes.get("email").toString()
                    val count = userRepository.count()

                    val allAuthorities = userAuthorityService
                            .allAuthorities()
                            .map { it.toName() }
                            .toSet()

                    val user = userRepository.findByReference(email)
                            .orElseGet {
                                val user = community.flock.eco.feature.user.model.User(
                                        reference = email,
                                        name = name,
                                        email = email,
                                        authorities = if (count == 0L) allAuthorities else setOf()
                                )
                                userRepository.save(user)
                            }

                    user.let {
                        it.authorities
                                .map { SimpleGrantedAuthority(it) }
                                .plus(SimpleGrantedAuthority("ROLE_USER"))
                    }

                }
        return http
    }

}