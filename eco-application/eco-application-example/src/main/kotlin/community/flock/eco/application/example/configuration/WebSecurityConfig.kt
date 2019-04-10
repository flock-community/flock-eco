package community.flock.eco.application.example.configuration

import community.flock.eco.application.example.authorities.ExampleAuthority
import community.flock.eco.feature.user.data.UserLoadData
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.user.services.UserSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User as UserDetail


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

    @Autowired
    lateinit var userLoadData: UserLoadData

    override fun configure(http: HttpSecurity) {

        userAuthorityService.addAuthority(ExampleAuthority::class.java)

        userLoadData.load(10)

        http
                .csrf().disable()
        http
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/_ah/**").permitAll()
        http
                .cors()
//        when(environment.activeProfiles) {
//            environment.activeProfiles.contains("local") -> userSecurityService.testLogin(http)
//        }
        userSecurityService.testLogin(http)
        //userSecurityService.googleLogin(http)

    }


}

