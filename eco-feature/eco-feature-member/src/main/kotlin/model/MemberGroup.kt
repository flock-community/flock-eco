package community.flock.eco.feature.member.model

import javax.persistence.*

@Entity
data class MemberGroup(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(unique = true)
    val code: String,
    val name: String

)
