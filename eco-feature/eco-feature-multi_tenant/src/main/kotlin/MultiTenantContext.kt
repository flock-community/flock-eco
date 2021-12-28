package community.flock.eco.feature.multi_tenant

import community.flock.eco.feature.multi_tenant.MultiTenantConstants.DEFAULT_TENANT

object MultiTenantContext {

    private val currentTenant: ThreadLocal<String> = InheritableThreadLocal()

    fun getCurrentTenant(): String = currentTenant
        .get()
        ?: DEFAULT_TENANT

    fun setCurrentTenant(tenant: String?) = currentTenant
        .set(tenant ?: DEFAULT_TENANT)

    fun clear() = currentTenant
        .set(null)
}
