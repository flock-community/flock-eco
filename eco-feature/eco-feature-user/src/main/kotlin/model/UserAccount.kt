package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import community.flock.eco.core.model.AbstractIdEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.ManyToOne

@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
abstract class UserAccount(

        @JsonManagedReference
        @ManyToOne
        open val user: User,

        val created: LocalDateTime = LocalDateTime.now()

) : AbstractIdEntity<Long>()
