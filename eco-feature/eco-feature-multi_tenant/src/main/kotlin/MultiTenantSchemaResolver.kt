package community.flock.eco.feature.multi_tenant

import org.hibernate.context.spi.CurrentTenantIdentifierResolver

class MultiTenantSchemaResolver : CurrentTenantIdentifierResolver {

    override fun resolveCurrentTenantIdentifier(): String {
        return MultiTenantContext.getCurrentTenant()
    }

    override fun validateExistingCurrentSessions(): Boolean {
        return true
    }
}
