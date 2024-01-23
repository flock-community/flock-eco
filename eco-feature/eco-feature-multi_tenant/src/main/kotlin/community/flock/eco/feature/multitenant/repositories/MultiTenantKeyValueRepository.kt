package community.flock.eco.feature.multitenant.repositories

import community.flock.eco.feature.multitenant.model.MultiTenantKeyValue
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface MultiTenantKeyValueRepository : PagingAndSortingRepository<MultiTenantKeyValue, String>
