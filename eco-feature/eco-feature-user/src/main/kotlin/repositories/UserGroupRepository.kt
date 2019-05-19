package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.UserGroup
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface UserGroupRepository : PagingAndSortingRepository<UserGroup, Long> {
    fun findByCode(code: String): Optional<UserGroup>
}
