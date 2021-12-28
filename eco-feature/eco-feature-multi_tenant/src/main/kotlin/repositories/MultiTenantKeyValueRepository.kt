package community.flock.eco.feature.multi_tenant.repositories

import community.flock.eco.feature.multi_tenant.model.MultiTenantKeyValue
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface MultiTenantKeyValueRepository : PagingAndSortingRepository<MultiTenantKeyValue, String>
