package community.flock.eco.feature.gcp.runtimeconfig.controllers

import community.flock.eco.feature.gcp.runtimeconfig.clients.RuntimeConfigurationClient
import community.flock.eco.feature.gcp.runtimeconfig.model.GcpRuntimeConfig
import community.flock.eco.feature.gcp.runtimeconfig.model.GcpRuntimeVariable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/gcp/runtimeconfig")
class GcpRuntimeconfigController(private val runtimeConfigurationClient: RuntimeConfigurationClient) {

    @GetMapping("/configs")
    @PreAuthorize("hasAuthority('GcpRuntimconfigConfigAuthority.READ')")
    fun getConfigs(): List<GcpRuntimeConfig> = runtimeConfigurationClient.getConfigsList().configs

    @GetMapping("/configs/{config}/variables")
    @PreAuthorize("hasAuthority('GcpRuntimconfigVariavbleAuthority.READ')")
    fun getVariables(@PathVariable config: String): List<GcpRuntimeVariable> = runtimeConfigurationClient.getVariablesList(config).variables

}
