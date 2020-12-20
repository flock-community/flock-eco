package community.flock.eco.application.example.configuration

import community.flock.eco.application.example.authorities.ExampleAuthority
import community.flock.eco.feature.user.filters.UserKeyTokenFilter
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.user.services.UserSecurityService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig(
    private val userAuthorityService: UserAuthorityService,
    private val userSecurityService: UserSecurityService,
    private val userKeyTokenFilter: UserKeyTokenFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {

        userAuthorityService.addAuthority(ExampleAuthority::class.java)

        http
            .csrf().disable()
        http
            .authorizeRequests()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/_ah/**").permitAll()
            .anyRequest().authenticated()
        http
            .cors()

        http
            .addFilterAfter(userKeyTokenFilter, BasicAuthenticationFilter::class.java)

        userSecurityService.testLogin(http)
    }
}
