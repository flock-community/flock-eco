package community.flock.eco.feature.multi_tenant.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class MultiTenantKeyValue(
        @Id
        val key: String,
        val value: String
)
