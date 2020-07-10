package community.flock.eco.feature.multi_tenant.events

import community.flock.eco.core.events.Event
import community.flock.eco.feature.multi_tenant.model.Tenant

abstract class MultiTenantEvent(open val entity: Tenant) : Event
