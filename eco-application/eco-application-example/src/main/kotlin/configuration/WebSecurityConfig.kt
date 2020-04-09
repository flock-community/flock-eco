package community.flock.eco.application.example.configuration

import community.flock.eco.application.example.authorities.ExampleAuthority
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.user.services.UserSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var environment: Environment

    @Autowired
    lateinit var userAuthorityService: UserAuthorityService

    @Autowired
    lateinit var userSecurityService: UserSecurityService

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

        userSecurityService.testLogin(http)

    }


}

