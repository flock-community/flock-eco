package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "code")
data class UserGroup(

        override val id: Long = 0,

        @Column(unique = true)
        val code: String = UUID.randomUUID().toString(),

        val name: String,

        @ManyToMany
        @JsonIdentityReference(alwaysAsId = true)
        val users: Set<User> = setOf(),

        val created: LocalDateTime = LocalDateTime.now()

) : AbstractIdEntity(id){
        override fun equals(other: Any?) = super.equals(other)
        override fun hashCode() = super.hashCode()
        override fun toString() = super.toString()
}

