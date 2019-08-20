package community.flock.eco.feature.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import community.flock.eco.core.events.EventEntityListeners
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.security.Principal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(EventEntityListeners::class)
data class User(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true)
        val code: String = UUID.randomUUID().toString(),

        val name: String? = null,

        @Column(unique = true)
        val email: String,

        val enabled: Boolean = true,

        @ElementCollection(fetch = FetchType.EAGER)
        val authorities: Set<String> = setOf(),

        @OneToMany
        val accounts: Set<UserAccount> = setOf(),

        val created: LocalDateTime = LocalDateTime.now()

) : Serializable{
    fun getPrincipal(): Principal = Principal { code }
}




