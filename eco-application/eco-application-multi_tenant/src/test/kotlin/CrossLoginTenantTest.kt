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
class CrossLoginTenantTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should not be able to cross login into tenant`() {


        val sessionA = MockHttpSession()
        val sessionB = MockHttpSession()

       val tenantA = register(sessionA)
       val tenantB = register(sessionB)

        login(sessionA, tenantA)
        login(sessionB, tenantB)

        me(sessionA, tenantA)
        me(sessionB, tenantB)

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/users/me")
                        .session(sessionA)
                        .header("X-TENANT", tenantB.tenantName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized)

    }

    fun register(session: MockHttpSession):RegistrationInput {
        val random = UUID.randomUUID().toString().replace("-", "_")
        val input = RegistrationInput(
                tenantName = random,
                email = "tenant@$random.nl",
                name = "Tenant de Tenant"
        )

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/tenants/register")
                        .session(session)
                        .header("X-TENANT", input.tenantName)
                        .content(objectMapper.writeValueAsBytes(input))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)

        return input
    }

    fun login(session: MockHttpSession, tenant:RegistrationInput) {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/login")
                        .session(session)
                        .header("X-TENANT", tenant.tenantName)
                        .content("username=${tenant.email}&password=password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection)

    }

    fun me(session: MockHttpSession, tenant:RegistrationInput){

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/users/me")
                        .session(session)
                        .header("X-TENANT", tenant.tenantName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }
}
