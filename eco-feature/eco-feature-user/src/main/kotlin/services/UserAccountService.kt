package community.flock.eco.feature.user.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForResetCodeException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUser
import community.flock.eco.feature.user.exceptions.UserAccountWithEmailExistsException
import community.flock.eco.feature.user.forms.UserAccountForm
import community.flock.eco.feature.user.forms.UserAccountOauthForm
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserAccountOauth
import community.flock.eco.feature.user.model.UserAccountPassword
import community.flock.eco.feature.user.repositories.UserAccountOauthRepository
import community.flock.eco.feature.user.repositories.UserAccountPasswordRepository
import community.flock.eco.feature.user.repositories.UserAccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAccountService(
        private val userService: UserService,
        private val passwordEncoder: PasswordEncoder,
        private val userAccountRepository: UserAccountRepository,
        private val userAccountOauthRepository: UserAccountOauthRepository,
        private val userAccountPasswordRepository: UserAccountPasswordRepository
) {

    fun findUserAccountPasswordByEmail(email: String) = userAccountPasswordRepository.findByUserEmailContainingIgnoreCase(email)
            .toNullable()

    fun findUserAccountByUserCode(code: String) = userAccountPasswordRepository.findByUserCode(code)

    fun findUserAccountByResetCode(resetCode: String) = userAccountPasswordRepository.findByResetCode(resetCode)

    fun findUserAccountOauthByReference(reference: String) = userAccountOauthRepository.findByReference(reference)
            .toNullable()

    fun createUserAccountPassword(form: UserAccountPasswordForm): UserAccountPassword = userService.findByEmail(form.email)
            .let {
                if (it == null) form.internalize()
                else throw UserAccountWithEmailExistsException(it)
            }
            .let(userAccountRepository::save)

    fun createUserAccountOauth(form: UserAccountOauthForm): UserAccountOauth = userService.findByEmail(form.email)
            .let {
                if (it == null) form.internalize()
                else throw UserAccountWithEmailExistsException(it)
            }
            .let(userAccountRepository::save)

    fun generateResetCodeForUserCode(code: String): String = findUserAccountByUserCode(code)
            .map { it.copy(resetCode = UUID.randomUUID().toString()) }
            .map { userAccountRepository.save(it) }
            .map { it.resetCode!! }
            .orElseThrow { UserAccountNotFoundForUser(code) }

    fun resetPasswordWithResetCode(resetCode: String, password: String): UserAccountPassword = findUserAccountByResetCode(resetCode)
            .map { it.copy(password = password, resetCode = null) }
            .map { userAccountRepository.save(it) }
            .orElseThrow { UserAccountNotFoundForResetCodeException(resetCode) }

    private fun UserAccountPasswordForm.internalize() = UserAccountPassword(
            user = createUser(),
            password = passwordEncoder.encode(password)
    )

    private fun UserAccountOauthForm.internalize() = UserAccountOauth(
            user = createUser(),
            provider = provider,
            reference = reference
    )

    private fun UserAccountForm.createUser(): User = userService.create(UserForm(
            email = email,
            name = name,
            authorities = authorities
    ))

}
