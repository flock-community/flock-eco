package community.flock.eco.feature.multi_tenant.services

import community.flock.eco.feature.multi_tenant.events.MultiTenantCreateEvent
import community.flock.eco.feature.multi_tenant.events.MultiTenantDeleteEvent
import community.flock.eco.feature.multi_tenant.model.Tenant
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

    private fun schemaName(name: String) = name
            .apply {
                if (contains("-")) {
                    throw error("Hyphen not allowed in tenant name")
                }
            }
            .run {
                Tenant(
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

    fun createTenant(name: String) = schemaName(name)
            .apply { jdbcTemplate.update("CREATE SCHEMA ${this.schema}") }
            .apply { jdbcTemplate.update("CREATE SEQUENCE ${this.schema}.HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1") }
            .apply { applicationEventPublisher.publishEvent(MultiTenantCreateEvent(this)) }


    fun deleteTenant(name: String) = schemaName(name)
            .apply { jdbcTemplate.update("DROP SCHEMA ${this.schema}") }
            .apply { applicationEventPublisher.publishEvent(MultiTenantDeleteEvent(this)) }

    fun findAllTenant() = jdbcTemplate
            .queryForList("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")
            .map { it["SCHEMA_NAME"] as String }
            .filter { it.startsWith("${prefix}_", true) }
            .map {
                Tenant(
                        name = it.replace("${prefix}_", "", true),
                        schema = it
                )
            }

}

