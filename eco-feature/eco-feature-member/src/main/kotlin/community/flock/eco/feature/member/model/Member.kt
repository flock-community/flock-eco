package community.flock.eco.feature.member.model

import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
data class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    val firstName: String,
    val infix: String? = null,
    val surName: String,

    val email: String? = null,

    val phoneNumber: String? = null,

    val street: String? = null,
    val houseNumber: String? = null,
    val houseNumberExtension: String? = null,
    val postalCode: String? = null,
    val city: String? = null,
    val country: String? = null,

    val gender: MemberGender? = null,
    val birthDate: LocalDate? = null,

    @ManyToMany()
    val groups: Set<MemberGroup> = setOf(),

    @ElementCollection
    val fields: Map<String, String> = mapOf(),

    val status: MemberStatus = MemberStatus.NEW,

    val created: LocalDate = LocalDate.now()
)
