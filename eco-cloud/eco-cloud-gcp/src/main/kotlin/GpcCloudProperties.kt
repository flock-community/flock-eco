package community.flock.eco.cloud.gcp

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "flock.eco.cloud.gpc")
class GpcCloudProperties(
    val enabled: Boolean
)
