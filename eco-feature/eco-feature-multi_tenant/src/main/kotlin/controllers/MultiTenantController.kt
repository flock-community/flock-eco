package community.flock.eco.feature.multi_tenant.controllers

import community.flock.eco.feature.multi_tenant.graphql.kotlin.TenantInput
import community.flock.eco.feature.multi_tenant.services.MultiTenantSchemaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tenants")
class MultiTenantController(
    private val multiTenantSchemaService: MultiTenantSchemaService
) {

    @PostMapping("/create")
    fun createTenant(@RequestBody input: TenantInput): ResponseEntity<Unit> = multiTenantSchemaService
        .createTenant(input.tenantName)
        .run { ResponseEntity.noContent().build() }
}
