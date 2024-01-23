package community.flock.eco.feature.multitenant.filters

import community.flock.eco.feature.multitenant.MultiTenantContext
import community.flock.eco.feature.multitenant.services.MultiTenantSchemaService
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MultiTenantFilter(
    private val multiTenantSchemaService: MultiTenantSchemaService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val tenantName = request.getHeader("X-TENANT")

        val tenantSession = request.session.getAttribute("tenant")
        if (tenantSession != null && tenantSession != tenantName) {
            response.sendError(401)
        }

        if (tenantName != null) {
            val tenant = multiTenantSchemaService.createMultiTenant(tenantName)
            MultiTenantContext.setCurrentTenant(tenant.schema)
        }

        filterChain.doFilter(request, response)

        if (tenantName != null) {
            MultiTenantContext.clear()
        }
    }
}
