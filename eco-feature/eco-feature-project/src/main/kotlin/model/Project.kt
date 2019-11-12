package community.flock.eco.feature.project.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import community.flock.eco.core.events.EventEntityListeners
import community.flock.eco.core.model.AbstractCodeEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.OneToMany

@Entity
@EntityListeners(EventEntityListeners::class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "code")
data class Project(

        override val id: Long = 0,

        override val code: String = UUID.randomUUID().toString(),

        val name: String? = null,

        @OneToMany
        val users: Set<ProjectUser> = setOf(),

        @OneToMany
        val groups: Set<ProjectGroup> = setOf(),

        val created: LocalDateTime = LocalDateTime.now()

) : AbstractCodeEntity(id, code) {
    override fun equals(other: Any?) = super.equals(other)
    override fun hashCode() = super.hashCode()
    override fun toString() = super.toString()
}
