package community.flock.eco.feature.user.model

import community.flock.eco.core.events.EventEntityListeners
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(EventEntityListeners::class)
data class UserAccountPassword(

        override val user: User,

        val password: String,
        val resetCode: String? = null

) : UserAccount(user = user) {
    fun getUserDetails(): UserDetails {
        val it = this
        return object : UserDetails {
            override fun getAuthorities() = user.authorities
                    .map { SimpleGrantedAuthority(it) }
                    .toList()

            override fun isEnabled() = user.enabled
            override fun getUsername() = user.email
            override fun getPassword() = it.password
            override fun isCredentialsNonExpired() = resetCode != null
            override fun isAccountNonExpired() = true
            override fun isAccountNonLocked() = true
        }
    }
}
