package community.flock.eco.feature.members.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class MemberGroup(

        @Id
        val code: String,
        val name: String
)
