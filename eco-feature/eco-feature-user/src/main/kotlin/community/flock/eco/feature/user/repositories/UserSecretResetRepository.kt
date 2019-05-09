package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.UserSecretReset
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service

@Service
interface UserSecretResetRepository : PagingAndSortingRepository<UserSecretReset, Long>
