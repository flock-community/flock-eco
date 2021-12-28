package community.flock.eco.application.multi_tenant

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.application.multi_tenant.controllers.RegistrationInput
import community.flock.eco.feature.multi_tenant.graphql.TenantInput
import community.flock.eco.feature.multi_tenant.services.MultiTenantSchemaService
import community.flock.eco.feature.user.model.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CreateMultiTenantTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var multiTenantSchemaService: MultiTenantSchemaService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun createTenant() {

        val input = TenantInput(
            tenantName = UUID.randomUUID().toString().replace("-", "_"),
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/tenants/create")
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent)

        val tenants = multiTenantSchemaService.findAllTenant()
        assertTrue(tenants.any { input.tenantName.equals(it.name, true) })
    }

}
