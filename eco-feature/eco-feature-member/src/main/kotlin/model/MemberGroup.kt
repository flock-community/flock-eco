package community.flock.eco.feature.member.model

import community.flock.eco.core.events.EventEntityListeners
import javax.persistence.*

@Entity
@EntityListeners(EventEntityListeners::class)
data class MemberGroup(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(unique = true)
    val code: String,
    val name: String

)
