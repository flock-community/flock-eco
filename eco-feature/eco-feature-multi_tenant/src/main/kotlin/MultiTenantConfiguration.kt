package community.flock.eco.feature.multi_tenant


import community.flock.eco.feature.multi_tenant.controllers.MultiTenantController
import community.flock.eco.feature.multi_tenant.filters.MultiTenantFilter
import community.flock.eco.feature.multi_tenant.services.MultiTenantSchemaService
import liquibase.integration.spring.MultiTenantSpringLiquibase
import org.hibernate.MultiTenancyStrategy
import org.hibernate.cfg.Environment
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Import
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource

@Import(
        MultiTenantController::class,
        MultiTenantSchemaService::class,
        MultiTenantFilter::class,
        MultiTenantSchemaResolver::class,
        MultiTenantConnectionProvider::class)
class MultiTenantConfiguration : WebMvcConfigurer {

    @Autowired
    lateinit var multitenantFilter: MultiTenantFilter

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(multitenantFilter)
    }

    @Bean
    @DependsOn("liquibase")
    fun liquibaseMt(
            dataSource: DataSource,
            multitenantSchemaService: MultiTenantSchemaService,
            liquibaseProperties: LiquibaseProperties): MultiTenantSpringLiquibase {
        val schemas = multitenantSchemaService
                .findAllTenant()
                .map { it.schema }
        return multitenantSchemaService.liquibase(schemas)
    }

    @Bean
    fun entityManagerFactoryBuilder(
            tenantConnectionProvider: MultiTenantConnectionProvider,
            multiTenantSchemaResolver: MultiTenantSchemaResolver,
            jpaVendorAdapter: JpaVendorAdapter,
            persistenceUnitManager: ObjectProvider<PersistenceUnitManager>,
            customizers: ObjectProvider<EntityManagerFactoryBuilderCustomizer>): EntityManagerFactoryBuilder {
        val properties = mapOf(
                Environment.MULTI_TENANT to MultiTenancyStrategy.SCHEMA,
                Environment.MULTI_TENANT_CONNECTION_PROVIDER to tenantConnectionProvider,
                Environment.MULTI_TENANT_IDENTIFIER_RESOLVER to multiTenantSchemaResolver
        )
        return EntityManagerFactoryBuilder(jpaVendorAdapter, properties, persistenceUnitManager.ifAvailable)
                .apply {
                    customizers.orderedStream()
                            .forEach { it.customize(this) }
                }

    }

}


