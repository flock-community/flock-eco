package community.flock.eco.feature.project.model

import community.flock.eco.core.events.EventEntityListeners
import community.flock.eco.core.model.AbstractIdEntity
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(EventEntityListeners::class)
data class ProjectRole(

        override val id: Long = 0,

        val name: String

) : AbstractIdEntity(id) {
    override fun equals(other: Any?) = super.equals(other)
    override fun hashCode() = super.hashCode()
    override fun toString() = super.toString()
}
