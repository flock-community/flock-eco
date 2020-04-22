package community.flock.eco.feature.member.model

import community.flock.eco.core.events.EventEntityListeners
import java.time.LocalDate
import javax.persistence.*

@Entity
@EntityListeners(EventEntityListeners::class)
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
        val language: String? = null,

        @Enumerated(EnumType.STRING)
        val gender: MemberGender? = null,
        val birthDate: LocalDate? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        val groups: Set<MemberGroup> = setOf(),

        @ElementCollection(fetch = FetchType.EAGER)
        val fields: Map<String, String> = mapOf(),

        @Enumerated(EnumType.STRING)
        val status: MemberStatus = MemberStatus.NEW,

        val created: LocalDate = LocalDate.now(),
        val updated: LocalDate = LocalDate.now()
)
