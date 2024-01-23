package community.flock.eco.feature.multitenant.events

import community.flock.eco.core.events.Event
import community.flock.eco.feature.multitenant.model.MultiTenant

abstract class MultiTenantEvent(open val entity: MultiTenant) : Event
