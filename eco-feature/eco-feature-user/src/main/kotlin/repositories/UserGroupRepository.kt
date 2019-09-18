package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.UserGroup
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserGroupRepository : PagingAndSortingRepository<UserGroup, Long> {

    fun findByCode(code: String): Optional<UserGroup>

}
