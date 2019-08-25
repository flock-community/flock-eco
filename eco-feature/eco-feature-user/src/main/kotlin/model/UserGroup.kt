package community.flock.eco.feature.user.model

import community.flock.eco.core.events.EventEntityListeners
import community.flock.eco.core.model.AbstractIdEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.ManyToMany

@Entity
@EntityListeners(EventEntityListeners::class)
data class UserGroup(

        override val id: Long = 0,

        @Column(unique = true)
        val code: String = UUID.randomUUID().toString(),

        val name: String,

        @ManyToMany
        val users: Set<User> = setOf(),

        val created: LocalDateTime = LocalDateTime.now()

) : AbstractIdEntity(id){
        override fun equals(other: Any?) = super.equals(other)
        override fun hashCode() = super.hashCode()
        override fun toString() = super.toString()
}

