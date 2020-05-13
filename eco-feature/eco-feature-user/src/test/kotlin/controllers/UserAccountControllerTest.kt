package controllers

import com.sun.org.apache.regexp.internal.RETest.test
import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserAccountService
import helpers.CreateHelper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [UserConfiguration::class])
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserAccountControllerTest {
    private val baseUrl: String = "/api/user-accounts"

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var createHelper: CreateHelper

    @Autowired
    private lateinit var userAccountService: UserAccountService

    @Test
    fun `user can update its own key`() {
        var userAccount = createHelper.createUser(setOf())
        var label = "My Generation"

        userAccountService.generateKeyForUserCode(userAccount.code,label)

        mvc.perform(put("$baseUrl/update-key")
                .with(user(CreateHelper.UserSecurity(userAccount)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }


    @Test
    fun `user can't update someone else key`() {

    }


}