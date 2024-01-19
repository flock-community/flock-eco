package community.flock.eco.cloud.gcp

import community.flock.eco.cloud.gcp.services.GpcMailService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(GpcMailService::class)
class GcpMailConfiguration
