package community.flock.eco.application.multi_tenant.config

import community.flock.eco.feature.user.filters.UserKeyTokenFilter
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.user.services.UserSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.POST
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


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
    lateinit var userKeyTokenFilter: UserKeyTokenFilter

    override fun configure(http: HttpSecurity) {

        http
                .csrf().disable()
        http
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/_ah/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/tenants/create").permitAll()
                .anyRequest().authenticated()
        http
                .cors()

        http
                .addFilterAfter(userKeyTokenFilter, BasicAuthenticationFilter::class.java)

        userSecurityService.testLogin(http)

    }


}

