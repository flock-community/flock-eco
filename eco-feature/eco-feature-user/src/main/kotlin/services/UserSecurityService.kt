package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.core.userdetails.User as UserDetail


const val DEFAULT_ROLE = "ROLE_USER"

class UserSecurityService(
        private val userAuthorityService: UserAuthorityService,
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    fun testLogin(http: HttpSecurity): FormLoginConfigurer<HttpSecurity>? {
        return http
                .userDetailsService { ref ->

                    User(
                            reference = ref,
                            name = ref,
                            email = ref,
                            authorities = allAuthorities()
                    ).let {
                        userRepository.findByReference(ref)
                                .orElseGet { userRepository.save(it) }

                    }.let { user ->
                        UserDetail.builder()
                                .username(ref)
                                .password(ref
                                        .let { passwordEncoder.encode(it) })
                                .authorities(user.getGrantedAuthority())
                                .build()
                    }
                }
                .formLogin()
    }

    fun databaseLogin(http: HttpSecurity): FormLoginConfigurer<HttpSecurity> {

        return http
                .userDetailsService { ref ->
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
                .formLogin()

    }

    fun googleLogin(http: HttpSecurity): OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig {

        return http
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService { it ->

                    val delegate = OidcUserService()
                    val oidcUser = delegate.loadUser(it)

                    val name = it.additionalParameters["name"].toString()
                    val email = it.additionalParameters["email"].toString()
                    val count = userRepository.count()

                    userRepository.findByReference(email)
                            .orElseGet {
                                User(
                                        reference = email,
                                        name = name,
                                        email = email,
                                        authorities = if (count == 0L) allAuthorities() else setOf()
                                ).let { user ->
                                    userRepository.save(user)
                                }

                            }
                            .let { user -> DefaultOidcUser(user.getGrantedAuthority(), oidcUser.idToken, oidcUser.userInfo, "email") }

                }
    }

    private fun User.getGrantedAuthority(): List<GrantedAuthority> {
        return this.authorities.map { SimpleGrantedAuthority(it) }
                .plus(SimpleGrantedAuthority(DEFAULT_ROLE))
    }

    private fun allAuthorities() = userAuthorityService
            .allAuthorities()
            .map { it.toName() }
            .toSet()

}



