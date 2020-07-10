package community.flock.eco.application.multi_tenant

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.eco.feature.multi_tenant.graphql.TenantInput
import community.flock.eco.feature.multi_tenant.services.MultiTenantSchemaService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.test.assertTrue


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CreateTenantTest {

    @Autowired
    lateinit var mockMvc:MockMvc

    @Autowired
    lateinit var multiTenantSchemaService: MultiTenantSchemaService

    @Test
    fun createTenant(){

        val input = TenantInput(
                tenantName = UUID.randomUUID().toString().replace("-","_"),
                userEmail = "test@${UUID.randomUUID()}.io"
        )

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/tenants/create")
                .content(input.toJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent)

        val tenants = multiTenantSchemaService.findAllTenant()

        assertTrue(tenants.any { input.tenantName.equals(it.name, true) })
    }
}

private fun TenantInput.toJson(): String = ObjectMapper()
        .writeValueAsString(this)

