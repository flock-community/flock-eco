package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import community.flock.eco.core.model.AbstractIdEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
abstract class UserAccount(

        override val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonIdentityReference(alwaysAsId = true)
        open val user: User,

        open val created: LocalDateTime = LocalDateTime.now()

) : AbstractIdEntity(id)
