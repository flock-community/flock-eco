package community.flock.eco.feature.multi_tenant.events

import community.flock.eco.feature.multi_tenant.model.MultiTenant

data class MultiTenantDeleteEvent(override val entity: MultiTenant) : MultiTenantEvent(entity)
