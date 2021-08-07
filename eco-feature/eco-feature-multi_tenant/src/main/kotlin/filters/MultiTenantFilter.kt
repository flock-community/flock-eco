package community.flock.eco.feature.multi_tenant.filters

import community.flock.eco.feature.multi_tenant.MultiTenantContext
import community.flock.eco.feature.multi_tenant.services.MultiTenantSchemaService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MultiTenantFilter(
       private val multiTenantSchemaService: MultiTenantSchemaService
        ): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tenantName = request.getHeader("X-TENANT")

        val tenantSession = request.session.getAttribute("tenant")
        if(tenantSession != null && tenantSession != tenantName){
            response.sendError(401)
        }

        if (tenantName != null) {
            val tenant = multiTenantSchemaService.schemaName(tenantName)
            MultiTenantContext.setCurrentTenant(tenant.schema)
        }

        filterChain.doFilter(request, response)

        if (tenantName != null) {
            MultiTenantContext.clear()
        }
    }
}
