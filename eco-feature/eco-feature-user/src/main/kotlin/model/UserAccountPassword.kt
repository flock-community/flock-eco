package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.*
import community.flock.eco.core.events.EventEntityListeners
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(EventEntityListeners::class)
class UserAccountPassword(

        override val id: Long = 0,

        @JsonIdentityReference(alwaysAsId = true)
        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "code")
        override val user: User,

        @JsonIgnore
        val secret: String? = null,

        @JsonIgnore
        val resetCode: String? = null

) : UserAccount(id, user)

