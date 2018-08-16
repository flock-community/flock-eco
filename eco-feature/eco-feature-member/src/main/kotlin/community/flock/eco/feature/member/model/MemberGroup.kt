package community.flock.eco.feature.member.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class MemberGroup(

        @Id
        val code: String,
        val name: String
)
