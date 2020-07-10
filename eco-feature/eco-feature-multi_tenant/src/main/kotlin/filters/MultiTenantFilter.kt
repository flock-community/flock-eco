package community.flock.eco.feature.multi_tenant.filters

import community.flock.eco.feature.multi_tenant.MultiTenantContext
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class MultiTenantFilter : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse,
                           obj: Any): Boolean {
        println("In preHandle we are Intercepting the Request ---")
        val tenantID = request.getParameter("tenant")

        if(request.requestURI == "/tenant"){
            MultiTenantContext.setCurrentTenant("TENANT_TEST")
        } else{
            MultiTenantContext.setCurrentTenant(tenantID)
        }

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?) {
        MultiTenantContext.clear()
    }
}
