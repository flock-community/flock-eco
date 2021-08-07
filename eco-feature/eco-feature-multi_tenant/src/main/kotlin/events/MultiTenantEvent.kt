package community.flock.eco.feature.multi_tenant.events

import community.flock.eco.core.events.Event
import community.flock.eco.feature.multi_tenant.model.MultiTenant

abstract class MultiTenantEvent(open val entity: MultiTenant) : Event
