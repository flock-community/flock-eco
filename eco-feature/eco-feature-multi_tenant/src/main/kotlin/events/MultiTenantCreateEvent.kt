package community.flock.eco.feature.multi_tenant.events

import community.flock.eco.feature.multi_tenant.model.MultiTenant

data class MultiTenantCreateEvent(override val entity: MultiTenant) : MultiTenantEvent(entity)
