package community.flock.eco.feature.gcp.runtimeconfig

import community.flock.eco.feature.gcp.runtimeconfig.controllers.GcpRuntimeconfigController
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(GcpRuntimeconfigController::class,
        GcpRuntimeconfigController::class)
class GcpRuntimeconfigConfiguration
