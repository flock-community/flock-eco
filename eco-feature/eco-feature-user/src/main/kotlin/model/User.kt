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

        @Column(unique = true)
        val reference: String,

        @JsonIgnore
        val secret: String? = null,

        val name: String,
        val email: String,

        val enabled: Boolean = true,

        @ElementCollection(fetch = FetchType.EAGER)
        val authorities: Set<String> = setOf(),

        val created: LocalDateTime = LocalDateTime.now(),
        val updated: LocalDateTime = LocalDateTime.now()

) : Serializable

fun User.getPrincipal(): Principal = Principal { this.email }
fun User.getUserDetails(): UserDetails {
    val user = this
     return object : UserDetails {
        override fun getAuthorities() = user.authorities
                .map { SimpleGrantedAuthority(it) }
                .toList()
        override fun isEnabled() = user.enabled
        override fun getUsername() = user.reference
        override fun getPassword() = user.secret
        override fun isCredentialsNonExpired() = true
        override fun isAccountNonExpired() = true
        override fun isAccountNonLocked() = true
    }
}

