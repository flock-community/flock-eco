package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.*
import community.flock.eco.core.model.AbstractIdEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(
    strategy = InheritanceType.JOINED
)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = UserAccountPassword::class, name = "PASSWORD"),
        JsonSubTypes.Type(value = UserAccountKey::class, name = "KEY"),
        JsonSubTypes.Type(value = UserAccountOauth::class, name = "OAUTH")
    ]
)
abstract class UserAccount(

    override val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "code")
    @JsonIdentityReference(alwaysAsId = true)
    open val user: User,

    open val created: LocalDateTime = LocalDateTime.now()

) : AbstractIdEntity(id)
