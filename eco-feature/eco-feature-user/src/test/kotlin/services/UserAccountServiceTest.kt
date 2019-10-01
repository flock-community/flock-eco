package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.exceptions.UserAccountExistsException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserCode
import community.flock.eco.feature.user.forms.UserAccountOauthForm
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.UserAccountOauthProvider
import community.flock.eco.feature.user.repositories.UserAccountPasswordRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [UserConfiguration::class])
@DataJpaTest
@AutoConfigureTestDatabase
class UserAccountServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userAccountService: UserAccountService

    @Autowired
    private lateinit var userAccountPasswordRepository: UserAccountPasswordRepository

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

        assertEquals(1, userService.findAll().count())
    }

    @Test(expected = UserAccountExistsException::class)
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

    @Test(expected = UserAccountNotFoundForUserCode::class)
    fun `generate reset code for user that doesn't exist`() {
        userAccountService.generateResetCodeForUserCode("doesn't exist")
    }

    @Test
    fun `reset password with wrong reset code`() {
        assertNull(userAccountService.resetPasswordWithResetCode("wrong!", "password"))
    }

    @Test
    fun `generate and reset password with reset code`() {
        val userAccount = userAccountService.createUserAccountPassword(passwordForm.copy())
        assertNull(userAccount.resetCode)
        userAccountService.generateResetCodeForUserCode(userAccount.user.code)
        val resetCode = userAccountService.findUserAccountPasswordByUserEmail(passwordForm.email)?.resetCode
        assertNotNull(resetCode)
        userAccountService.resetPasswordWithResetCode(resetCode!!, "password")
        val account = userAccountService.findUserAccountPasswordByUserEmail(passwordForm.email)!!
        assertNull(account.resetCode)
        assertTrue(passwordEncoder.matches("password", account.secret))
    }

    @Test
    fun `request reset via email`() {
        assertNull(userAccountService.createUserAccountPassword(passwordForm.copy()).resetCode)
        userAccountService.generateResetCodeForUserEmail(passwordForm.email)
        assertNotNull(userAccountService.findUserAccountPasswordByUserEmail(passwordForm.email)?.resetCode)
    }

    @Test
    fun `create user account password without password`() {
        val user = userService.create(UserForm(
                name="Pino",
                email = "pino@sesamstreet.xx"
        ))
        userAccountService.createUserAccountPasswordWithoutPassword(user.code)

        val account = userAccountService.findUserAccountPasswordByUserEmail(user.email)

        assertNotNull(account)

    }

    @Test(expected = UserAccountExistsException::class)
    fun `create user account password without password create twice`() {
        val user = userService.create(UserForm(
                name="Pino",
                email = "pino@sesamstreet.xx"
        ))
        userAccountService.createUserAccountPasswordWithoutPassword(user.code)
        userAccountService.createUserAccountPasswordWithoutPassword(user.code)

    }

    @Test
    fun `delete user with account password`() {
        val account = userAccountService.createUserAccountPassword(passwordForm.copy())

        userService.delete(account.user.code)

        Assert.assertNull(userService.findByCode(account.user.code))
        Assert.assertEquals(0, userAccountPasswordRepository.findAll().count())
    }

}
