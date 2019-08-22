package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
abstract class UserAccount(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @JsonIgnore
        @ManyToOne
        open val user: User,

        val created: LocalDateTime = LocalDateTime.now()

) : Serializable
