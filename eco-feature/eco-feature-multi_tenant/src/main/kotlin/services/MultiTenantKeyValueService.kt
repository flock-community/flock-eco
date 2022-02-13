package community.flock.eco.feature.multi_tenant.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.multi_tenant.model.MultiTenantKeyValue
import community.flock.eco.feature.multi_tenant.repositories.MultiTenantKeyValueRepository
import org.springframework.stereotype.Service

@Service
class MultiTenantKeyValueService(
    private val multiTenantKeyValueRepository: MultiTenantKeyValueRepository
) {

    fun findAll(): Iterable<MultiTenantKeyValue> {
        return multiTenantKeyValueRepository.findAll()
    }

    fun findByKey(key: String): MultiTenantKeyValue? {
        return multiTenantKeyValueRepository.findById(key).toNullable()
    }

    fun save(keyValue: MultiTenantKeyValue) {
        multiTenantKeyValueRepository.save(keyValue)
    }
}
