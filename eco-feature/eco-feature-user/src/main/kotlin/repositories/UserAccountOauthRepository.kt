package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.UserAccountOauth
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.*

@Repository
interface UserAccountOauthRepository : PagingAndSortingRepository<UserAccountOauth, Long> {
    fun findByReference(reference: String): Optional<UserAccountOauth>
}
