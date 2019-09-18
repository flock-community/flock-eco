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

    private val passwordForm = UserAccountPasswordForm(
            name = "Willem Veelenturf",
            email = "willem.veelenturf@gmail.com",
            password = "123456"
    )

    @Test
    fun `register user with password`() {
        val (id, user, password) = userAccountService.createUserAccountPassword(passwordForm)

        assertNotNull(id)
        assertNotNull(user.id)
        assertNotNull(user.code)

        assertTrue(passwordEncoder.matches(passwordForm.password, password))
    }

    @Test(expected = UserAccountWithEmailExistsException::class)
    fun `register user with password twice`() {
        userAccountService.createUserAccountPassword(passwordForm.copy())
        userAccountService.createUserAccountPassword(passwordForm.copy())
    }

    @Test
    fun `test register oauth user`() {
        val form = UserAccountOauthForm(
                name = passwordForm.name,
                email = passwordForm.email,
                provider = UserAccountOauthProvider.GOOGLE,
                reference = "123123123"
        )
        val (id, user, reference) = userAccountService.createUserAccountOauth(form)

        assertNotNull(id)
        assertNotNull(user.id)
        assertNotNull(user.code)

        assertEquals(form.reference, reference)
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
        val userAccount = userAccountService.createUserAccountPassword(passwordForm.copy())
        assertNull(userAccount.resetCode)
        val resetCode = userAccountService.generateResetCodeForUserCode(userAccount.user.code)
        assertNotNull(userAccountService.findUserAccountByResetCode(resetCode)?.resetCode)
        val account = userAccountService.resetPasswordWithResetCode(resetCode, "password")
        assertNull(account.resetCode)
        assertTrue(passwordEncoder.matches("password", account.password))
    }

    @Test
    fun `request reset via email`() {
        assertNull(userAccountService.createUserAccountPassword(passwordForm.copy()).resetCode)
        userAccountService.requestPasswordReset(passwordForm.email)
        assertNotNull(userAccountService.findUserAccountPasswordByEmail(passwordForm.email)?.resetCode)
    }

}
