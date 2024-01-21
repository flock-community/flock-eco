package community.flock.eco.application.multitenant

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.application.multitenant.controllers.RegistrationInput
import community.flock.eco.feature.multitenant.services.MultiTenantSchemaService
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RegisterTenantTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var multiTenantSchemaService: MultiTenantSchemaService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun registerTenant() {
        val random = UUID.randomUUID().toString().replace("-", "_")
        val input =
            RegistrationInput(
                tenantName = random,
                email = "tenant@$random.nl",
                name = "Tenant de Tenant",
            )

        val session = MockHttpSession()

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/tenants/register")
                .session(session)
                .header("X-TENANT", input.tenantName)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login")
                .session(session)
                .header("X-TENANT", input.tenantName)
                .content("username=${input.email}&password=password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/users/me")
                .session(session)
                .header("X-TENANT", input.tenantName),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.email")
                    .value(input.email),
            )
    }
}
