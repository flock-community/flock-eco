package community.flock.eco.feature.multi_tenant

object MultiTenantContext {

    private const val defaultTenant = "PUBLIC"

    private val currentTenant: ThreadLocal<String> = InheritableThreadLocal()

    fun getCurrentTenant(): String = currentTenant
            .get()
            ?: defaultTenant

    fun setCurrentTenant(tenant: String?) = currentTenant
            .set(tenant ?: defaultTenant)


    fun clear() = currentTenant
            .set(null)

}
