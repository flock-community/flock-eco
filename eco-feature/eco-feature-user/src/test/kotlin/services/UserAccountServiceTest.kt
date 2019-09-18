package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForResetCodeException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUser
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase
class UserAccountServiceTest {

    @Autowired
    private lateinit var userAccountService: UserAccountService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `register user with password`() {
        val form = UserAccountPasswordForm(
                name = "Willem Veelenturf",
                email = "willem.veelenturf@gmail.com",
                password = "123456"
        )
        val res = userAccountService.createUserAccountPassword(form)

        assertNotNull(res.id)
        assertNotNull(res.user.id)
        assertNotNull(res.user.code)

        assertTrue(passwordEncoder.matches("123456", res.password))
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

        assertNotNull(res.id)
        assertNotNull(res.user.id)
        assertNotNull(res.user.code)

        assertEquals("123123123", res.reference)
    }

    @Test(expected = UserAccountNotFoundForUser::class)
    fun `generate reset code for user that doesn't exist`() {
        userAccountService.generateResetCodeForUserCode("doesn't exist")
    }

    @Test(expected = UserAccountNotFoundForResetCodeException::class)
    fun `reset password with wrong reset code`() {
        userAccountService.resetPasswordWithResetCode("wrong!", "password")
    }

    @Test
    fun `generate and reset password with reset code`() {
        val form = UserAccountPasswordForm(
                name = "Willem Veelenturf",
                email = "willem.veelenturf@gmail.com",
                password = "123456"
        )
        val user = userAccountService.createUserAccountPassword(form.copy()).user
        val resetCode = userAccountService.generateResetCodeForUserCode(user.code)
        val account = userAccountService.resetPasswordWithResetCode(resetCode, "password")
        assertNull(account.resetCode)
        assertTrue(passwordEncoder.matches("password", account.password))
    }

}
