package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import community.flock.eco.core.events.EventEntityListeners
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(EventEntityListeners::class)
data class UserAccountPassword(

        override val id: Long = 0,

        override val user: User,

        @JsonIgnore
        val secret: String,

        @JsonIgnore
        val resetCode: String? = null

) : UserAccount(id, user){
        override fun equals(other: Any?) = super.equals(other)
        override fun hashCode() = super.hashCode()
        override fun toString() = super.toString()
}

