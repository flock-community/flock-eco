package community.flock.eco.feature.gcp.sql.controllers

import community.flock.eco.feature.gcp.sql.clients.GcpSqlClient
import community.flock.eco.feature.gcp.sql.model.GcpSqlInstance
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/gcp/sql")
class GcpSqlController(private val runtimeConfigurationClient: GcpSqlClient) {

    @GetMapping("/instances")
    @PreAuthorize("hasAuthority('GcpRuntimconfigConfigAuthority.READ')")
    fun getConfigs(): List<GcpSqlInstance> = runtimeConfigurationClient.getInstaceList().items?: listOf()

}
