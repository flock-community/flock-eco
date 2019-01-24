package community.flock.eco.feature.gcp.sql

import community.flock.eco.feature.gcp.sql.clients.GcpSqlClient
import community.flock.eco.feature.gcp.sql.controllers.GcpSqlController
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(GcpSqlClient::class,
        GcpSqlController::class)
class GcpSqlConfiguration
