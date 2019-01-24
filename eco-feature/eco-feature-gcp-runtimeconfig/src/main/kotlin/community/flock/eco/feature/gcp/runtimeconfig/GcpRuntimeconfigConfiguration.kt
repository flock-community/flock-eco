package community.flock.eco.feature.gcp.runtimeconfig

import community.flock.eco.feature.gcp.runtimeconfig.clients.GcpRuntimeConfigurationClient
import community.flock.eco.feature.gcp.runtimeconfig.controllers.GcpRuntimeconfigController
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(GcpRuntimeConfigurationClient::class,
        GcpRuntimeconfigController::class)
class GcpRuntimeconfigConfiguration
