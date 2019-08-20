package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.exceptions.UserAccountWithEmailExistsException
import community.flock.eco.feature.user.forms.UserAccountOauthForm
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.model.UserAccountOauthProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase
class UserAccountServiceTest {

    @Autowired
    lateinit var userAccountService: UserAccountService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `register user with password`() {
        val form = UserAccountPasswordForm(
                name = "Willem Veelenturf",
                email = "willem.veelenturf@gmail.com",
                password = "123456"
        )
        val res = userAccountService.createUserAccountPassword(form)

        assertNotNull(res!!.id)
        assertNotNull(res!!.user.id)
        assertNotNull(res!!.user.code)

        assertTrue(passwordEncoder.matches("123456", res!!.password))
    }

    @Test(expected = UserAccountWithEmailExistsException::class)
    fun `register user with password twice`() {
        val form = UserAccountPasswordForm(
                name = "Willem Veelenturf",
                email = "willem.veelenturf@gmail.com",
                password = "123456"
        )
        userAccountService.createUserAccountPassword(form.copy())
        userAccountService.createUserAccountPassword(form.copy())
    }

    @Test
    fun `test register oauth user`() {
        val form = UserAccountOauthForm(
                name = "Willem Veelenturf",
                email = "willem.veelenturf@gmail.com",
                provider = UserAccountOauthProvider.GOOGLE,
                reference = "123123123"
        )
        val res = userAccountService.createUserAccountOauth(form)

        assertNotNull(res!!.id)
        assertNotNull(res!!.user.id)
        assertNotNull(res!!.user.code)

        assertEquals("123123123", res!!.reference)
    }
}
