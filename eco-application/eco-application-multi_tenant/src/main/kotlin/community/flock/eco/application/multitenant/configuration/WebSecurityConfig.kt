package community.flock.eco.application.multitenant.configuration

import community.flock.eco.feature.multitenant.filters.MultiTenantFilter
import community.flock.eco.feature.user.filters.UserKeyTokenFilter
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.user.services.UserSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userAuthorityService: UserAuthorityService

    @Autowired
    lateinit var userSecurityService: UserSecurityService

    @Autowired
    lateinit var userKeyTokenFilter: UserKeyTokenFilter

    @Autowired
    lateinit var multiTenantFilter: MultiTenantFilter

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
        http
            .authorizeRequests()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/api/tenants/register").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/tenants/create").permitAll()
            .anyRequest().authenticated()
        http
            .cors()

        http
            .addFilterBefore(multiTenantFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(userKeyTokenFilter, BasicAuthenticationFilter::class.java)

        userSecurityService
            .databaseLogin(http)
            .successHandler { request, response, _ ->
                request.session.setAttribute("tenant", request.getHeader("X-TENANT"))
                response.sendRedirect(request.contextPath)
            }
    }
}
