package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserAccount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service

import java.util.*

@Service
interface UserAccountRepository : PagingAndSortingRepository<UserAccount, Long> {

}
