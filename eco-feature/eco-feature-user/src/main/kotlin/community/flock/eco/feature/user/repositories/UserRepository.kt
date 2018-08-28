package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.User
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
interface UserRepository : PagingAndSortingRepository<User, Long> {

    fun findByReference(name: String): Optional<User>

}


