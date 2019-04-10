package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.core.userdetails.User as UserDetail

val DEFAULT_ROLE = "ROLE_USER"

class UserSecurityService(
        val userAuthorityService: UserAuthorityService,
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder
) {

    fun testLogin(http: HttpSecurity): HttpSecurity {

        http.userDetailsService { ref ->

            User(
                    reference = ref,
                    name = ref,
                    email = ref,
                    authorities = userAuthorityService.allAuthorities()
                            .map { it.toName() }
                            .toSet()
            ).let {
                userRepository.save(it)
            }.let { user ->
                UserDetail.builder()
                        .username(ref)
                        .password(ref
                                .let { passwordEncoder.encode(it) })
                        .authorities(*user.authorities
                                .map { SimpleGrantedAuthority(it) }
                                .plus(SimpleGrantedAuthority(DEFAULT_ROLE))
                                .toTypedArray())
                        .build()
            }
        }

        http.formLogin()

        return http
    }

    fun databaseLogin(http: HttpSecurity): HttpSecurity {

        http.userDetailsService { ref ->
            userRepository.findByReference(ref)
                    .map {
                        UserDetail.builder()
                                .username(it.reference)
                                .password(it.secret)
                                .authorities(it.getGrantedAuthority())
                                .build()
                    }
                    .orElseThrow { UsernameNotFoundException("User '$ref' not found") }
        }

        http.formLogin()

        return http
    }

    fun googleLogin(http: HttpSecurity): HttpSecurity {

        http
                .oauth2Login()
                .userInfoEndpoint()
                .userAuthoritiesMapper { it ->

                    val authority = it.first() as OAuth2UserAuthority
                    val name = authority.attributes.get("name").toString()
                    val email = authority.attributes.get("email").toString()
                    val count = userRepository.count()

                    val allAuthorities = userAuthorityService
                            .allAuthorities()
                            .map { it.toName() }
                            .toSet()

                    userRepository.findByReference(email)
                            .orElseGet {
                                User(
                                        reference = email,
                                        name = name,
                                        email = email,
                                        authorities = if (count == 0L) allAuthorities else setOf()
                                ).let { user ->
                                    userRepository.save(user)
                                }

                            }
                            .let { user -> user.getGrantedAuthority() }

                }
        return http
    }

    private fun User.getGrantedAuthority(): List<out GrantedAuthority> {
        return this.authorities.map { SimpleGrantedAuthority(it) }
                .plus(SimpleGrantedAuthority(DEFAULT_ROLE))
    }

}



