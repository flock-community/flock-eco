package community.flock.eco.feature.user.model

import community.flock.eco.core.events.EventEntityListeners
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(EventEntityListeners::class)
data class UserAccountOauth(

        override val id: Long = 0,

        override val user: User,

        val provider: UserAccountOauthProvider,
        val reference: String

) : UserAccount(id, user){
        override fun equals(other: Any?) = super.equals(other)
        override fun hashCode() = super.hashCode()
        override fun toString() = super.toString()
}

