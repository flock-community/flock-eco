package community.flock.eco.feature.multi_tenant.events

import community.flock.eco.feature.multi_tenant.model.Tenant

data class MultiTenantDeleteEvent(override val entity: Tenant) : MultiTenantEvent(entity)
