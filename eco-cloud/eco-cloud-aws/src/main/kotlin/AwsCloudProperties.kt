package community.flock.eco.cloud.aws

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "flock.eco.cloud.aws")
class AwsCloudProperties(
    val enabled: Boolean
)
