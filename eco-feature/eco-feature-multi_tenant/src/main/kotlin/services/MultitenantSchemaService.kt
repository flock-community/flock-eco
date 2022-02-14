package community.flock.eco.feature.multi_tenant.services

import community.flock.eco.feature.multi_tenant.MultiTenantConstants.DEFAULT_TENANT
import community.flock.eco.feature.multi_tenant.events.MultiTenantCreateEvent
import community.flock.eco.feature.multi_tenant.events.MultiTenantDeleteEvent
import community.flock.eco.feature.multi_tenant.model.MultiTenant
import liquibase.integration.spring.MultiTenantSpringLiquibase
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.Connection
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Service
class MultiTenantSchemaService(
    private val dataSource: DataSource,
    private val jdbcTemplate: JdbcTemplate,
    private val liquibaseProperties: LiquibaseProperties,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    val prefix = "TENANT"

    lateinit var databaseName: String

    private val lowerCase = arrayOf("PostgreSQL")
    private val upperCase = arrayOf("H2")

    @PostConstruct
    fun init() {
        val connection: Connection = dataSource.connection
        databaseName = connection.metaData.databaseProductName
        connection.close()
    }

    fun createMultiTenant(name: String): MultiTenant {
        if (name.contains("-")) {
            throw error("Hyphen not allowed in tenant name")
        }
        val schemaName = "${prefix}_${name.uppercase()}"
        return MultiTenant(
            name = name,
            schema = when {
                lowerCase.contains(databaseName) -> schemaName.lowercase()
                upperCase.contains(databaseName) -> schemaName.uppercase()
                else -> schemaName
            }
        )
    }

    fun liquibase(schemas: List<String>): MultiTenantSpringLiquibase {
        val liquibase = MultiTenantSpringLiquibase()
        liquibase.changeLog = liquibaseProperties.changeLog
        liquibase.dataSource = dataSource
        liquibase.defaultSchema = DEFAULT_TENANT
        liquibase.schemas = schemas
        return liquibase
    }

    // TODO: input validation
    fun createTenant(name: String) {
        val multiTenant = createMultiTenant(name)
        jdbcTemplate.update("CREATE SCHEMA ${multiTenant.schema}")
        applicationEventPublisher.publishEvent(MultiTenantCreateEvent(multiTenant))
    }

    // TODO: input validation
    fun deleteTenant(name: String) {
        val multiTenant = createMultiTenant(name)
        jdbcTemplate.update("DROP SCHEMA ${multiTenant.schema}")
        applicationEventPublisher.publishEvent(MultiTenantDeleteEvent(multiTenant))
    }

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
