package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserAccountPassword
import community.flock.eco.feature.user.repositories.UserAccountOauthRepository
import community.flock.eco.feature.user.repositories.UserAccountPasswordRepository
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser

class UserSecurityService(
        private val userService: UserService,
        private val userAuthorityService: UserAuthorityService,
        private val userAccountService: UserAccountService,
        private val passwordEncoder: PasswordEncoder
) {

    fun testLogin(http: HttpSecurity): FormLoginConfigurer<HttpSecurity>? {
        return http
                .userDetailsService { ref ->
                    val user = User(email = "$ref@$ref.nl")
                    val password = passwordEncoder.encode(ref)
                    val account = UserAccountPassword(user = user, password = password)
                    account.getUserDetails()
                }
                .formLogin()
    }

    fun databaseLogin(http: HttpSecurity): FormLoginConfigurer<HttpSecurity> {

        return http
                .userDetailsService { ref ->
                    userAccountService.findUserAccountPasswordByEmail(ref)
                            ?.let { it.getUserDetails() }
                            ?: throw UsernameNotFoundException("User '$ref' not found")
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

                    val name = oidcUser.attributes["name"].toString()
                    val email = oidcUser.attributes["email"].toString()

                    DefaultOidcUser(allAuthorities(), oidcUser.idToken, oidcUser.userInfo, "email")

                }
    }


    private fun User.getGrantedAuthority(): List<GrantedAuthority> {
        return this.authorities
                .map { SimpleGrantedAuthority(it) }
                .toList()
    }

    private fun allAuthorities() = userAuthorityService
            .allAuthorities()
            .map { it.toName() }
            .map { SimpleGrantedAuthority(it) }
            .toSet()

}



