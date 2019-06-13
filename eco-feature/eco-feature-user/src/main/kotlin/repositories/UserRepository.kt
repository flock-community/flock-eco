package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service

import java.util.*

@Service
interface UserRepository : PagingAndSortingRepository<User, Long> {

    fun findAllByNameLikeOrEmailLike(name: String, email: String, pageable: Pageable): Page<User>
    fun findByCode(code: String): Optional<User>
    fun findByReference(ref: String): Optional<User>

}
