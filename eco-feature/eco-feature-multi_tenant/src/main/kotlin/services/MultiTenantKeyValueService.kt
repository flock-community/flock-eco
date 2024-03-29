package community.flock.eco.feature.multitenant.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.multitenant.model.MultiTenantKeyValue
import community.flock.eco.feature.multitenant.repositories.MultiTenantKeyValueRepository
import org.springframework.stereotype.Service

@Service
class MultiTenantKeyValueService(
    private val multiTenantKeyValueRepository: MultiTenantKeyValueRepository,
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

    fun saveAll(keyValue: List<MultiTenantKeyValue>) {
        multiTenantKeyValueRepository.saveAll(keyValue)
    }
}
