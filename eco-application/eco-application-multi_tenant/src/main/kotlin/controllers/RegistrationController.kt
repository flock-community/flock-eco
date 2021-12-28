package community.flock.eco.application.multi_tenant.controllers

import community.flock.eco.feature.multi_tenant.MultiTenantContext
import community.flock.eco.feature.multi_tenant.model.MultiTenantKeyValue
import community.flock.eco.feature.multi_tenant.services.MultiTenantKeyValueService
import community.flock.eco.feature.multi_tenant.services.MultiTenantSchemaService
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.services.UserAccountService
import liquibase.integration.spring.MultiTenantSpringLiquibase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class RegistrationInput(
        val tenantName: String,
        val email: String,
        val name: String,
)

@RestController
@RequestMapping("/api/tenants")
class RegistrationController(
        private val multiTenantSchemaService: MultiTenantSchemaService,
        private val multiTenantKeyValueService: MultiTenantKeyValueService,
        private val multiTenantSpringLiquibase: MultiTenantSpringLiquibase,
        private val userAccountService: UserAccountService,
) {
    @PostMapping("/register")
    fun register(@RequestBody input: RegistrationInput) {

        // Find all tenants
        val tenant = multiTenantSchemaService.createMultiTenant(input.tenantName)
        multiTenantSchemaService.createTenant(input.tenantName)

        // Deploy liquibase schema
        multiTenantSpringLiquibase.schemas = listOf(tenant.schema)
        multiTenantSpringLiquibase.afterPropertiesSet()

        // Create User account
        userAccountService.createUserAccountPassword(UserAccountPasswordForm(
                email = input.email,
                password = "password"
        ))

        // Create Tenant Key Value
        multiTenantKeyValueService.save(MultiTenantKeyValue(
                key = "NAME",
                value = tenant.name
        ))

    }
}
