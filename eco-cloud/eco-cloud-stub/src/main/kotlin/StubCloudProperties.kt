package community.flock.eco.cloud.stub

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "flock.eco.cloud.stub")
class StubCloudProperties(
    val enabled: Boolean
)
