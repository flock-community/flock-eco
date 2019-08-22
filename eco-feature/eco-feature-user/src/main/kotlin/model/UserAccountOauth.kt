package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import community.flock.eco.core.events.EventEntityListeners
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@DiscriminatorValue("UserAccount")
@EntityListeners(EventEntityListeners::class)
data class UserAccountOauth(

        @JsonIgnore
        override val user: User,

        val provider: UserAccountOauthProvider,
        val reference: String

) : UserAccount(user = user)
