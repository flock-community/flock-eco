package community.flock.eco.configuration.security.configuration

import community.flock.eco.feature.user.utils.UserSecurityUtil
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
class UserWebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var environment: Environment

    @Autowired
    lateinit var userSecurityUtil: UserSecurityUtil

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/_ah/**").permitAll()
        http
                .cors()

        if (environment.activeProfiles.contains("local"))
            userSecurityUtil.localLogin(http)
        else
            userSecurityUtil.googleLogin(http)
    }


}

