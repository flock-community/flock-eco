package community.flock.eco.feature.multitenant.events

import community.flock.eco.feature.multitenant.model.MultiTenant

data class MultiTenantDeleteEvent(override val entity: MultiTenant) : MultiTenantEvent(entity)
