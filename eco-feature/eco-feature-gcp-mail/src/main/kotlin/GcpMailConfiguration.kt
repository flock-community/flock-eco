package community.flock.eco.feature.gcp.mail

import community.flock.eco.feature.gcp.mail.services.GpcMailService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(GpcMailService::class)
class GcpMailConfiguration
