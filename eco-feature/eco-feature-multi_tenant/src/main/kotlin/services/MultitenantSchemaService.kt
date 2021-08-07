package community.flock.eco.feature.multi_tenant.services

import community.flock.eco.feature.multi_tenant.events.MultiTenantCreateEvent
import community.flock.eco.feature.multi_tenant.events.MultiTenantDeleteEvent
import community.flock.eco.feature.multi_tenant.model.MultiTenant
import liquibase.integration.spring.MultiTenantSpringLiquibase
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

class MultiTenantSchemaService(
    private val dataSource: DataSource,
    private val jdbcTemplate: JdbcTemplate,
    private val liquibaseProperties: LiquibaseProperties,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    val prefix = "TENANT"

    fun schemaName(name: String) = name
        .apply {
            if (contains("-")) {
                throw error("Hyphen not allowed in tenant name")
            }
        }
        .run {
            MultiTenant(
                name = name,
                schema = "${prefix}_${this.toUpperCase()}"
            )
        }

    fun liquibase(schemas: List<String>): MultiTenantSpringLiquibase {
        val liquibase = MultiTenantSpringLiquibase()
        liquibase.changeLog = liquibaseProperties.changeLog
        liquibase.dataSource = dataSource
        liquibase.defaultSchema = "PUBLIC"
        liquibase.schemas = schemas
        return liquibase
    }

    // TODO: input validation
    fun createTenant(name: String) = schemaName(name)
        .apply { jdbcTemplate.update("CREATE SCHEMA $schema") }
        .apply { jdbcTemplate.update("CREATE SEQUENCE $schema.HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1") }
        .apply { applicationEventPublisher.publishEvent(MultiTenantCreateEvent(this)) }

    // TODO: input validation
    fun deleteTenant(name: String) = schemaName(name)
        .apply { jdbcTemplate.update("DROP SCHEMA $schema") }
        .apply { applicationEventPublisher.publishEvent(MultiTenantDeleteEvent(this)) }

    // TODO: input validation
    fun findAllTenant() = jdbcTemplate
        .queryForList("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")
        .map { it["SCHEMA_NAME"] as String }
        .filter { it.startsWith("${prefix}_", true) }
        .map {
            MultiTenant(
                name = it.replace("${prefix}_", "", true),
                schema = it
            )
        }
}
