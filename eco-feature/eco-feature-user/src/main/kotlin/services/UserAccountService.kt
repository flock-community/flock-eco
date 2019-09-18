package community.flock.eco.feature.user.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.events.UserAccountPasswordResetEvent
import community.flock.eco.feature.user.events.UserAccountPasswordResetRequestEvent
import community.flock.eco.feature.user.events.UserAccountResetCodeGeneratedEvent
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
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserAccountService(
        private val userService: UserService,
        private val passwordEncoder: PasswordEncoder,
        private val userAccountRepository: UserAccountRepository,
        private val userAccountOauthRepository: UserAccountOauthRepository,
        private val userAccountPasswordRepository: UserAccountPasswordRepository,
        private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun findUserAccountPasswordByEmail(email: String) = userAccountPasswordRepository.findByUserEmailContainingIgnoreCase(email).toNullable()

    fun findUserAccountByUserCode(code: String) = userAccountPasswordRepository.findByUserCode(code).toNullable()

    fun findUserAccountByResetCode(resetCode: String) = userAccountPasswordRepository.findByResetCode(resetCode).toNullable()

    fun findUserAccountOauthByReference(reference: String) = userAccountOauthRepository.findByReference(reference).toNullable()

    fun createUserAccountPassword(form: UserAccountPasswordForm): UserAccountPassword = userService.findByEmail(form.email)
            ?.let { throw UserAccountWithEmailExistsException(it) }
            .let { form.createUserAndInternalize() }
            .let(userAccountRepository::save)

    fun createUserAccountOauth(form: UserAccountOauthForm): UserAccountOauth = userService.findByEmail(form.email)
            ?.let { throw UserAccountWithEmailExistsException(it) }
            .let { form.createUserAndInternalize() }
            .let(userAccountRepository::save)

    fun generateResetCodeForUserCode(code: String): String = findUserAccountByUserCode(code)
            ?.copy(resetCode = UUID.randomUUID().toString())
            ?.let(userAccountRepository::save)
            ?.also { applicationEventPublisher.publishEvent(UserAccountResetCodeGeneratedEvent(it)) }
            ?.run { resetCode!! }
            ?: throw UserAccountNotFoundForUser(code)

    fun requestPasswordReset(email: String) = findUserAccountPasswordByEmail(email)
            ?.copy(resetCode = UUID.randomUUID().toString())
            ?.let(userAccountRepository::save)
            ?.also { applicationEventPublisher.publishEvent(UserAccountPasswordResetRequestEvent(it)) }
            .let { Unit }

    fun resetPasswordWithResetCode(resetCode: String, password: String): UserAccountPassword = findUserAccountByResetCode(resetCode)
            ?.copy(password = password.encode(), resetCode = null)
            ?.let(userAccountRepository::save)
            ?.also { applicationEventPublisher.publishEvent(UserAccountPasswordResetEvent(it)) }
            ?: throw UserAccountNotFoundForResetCodeException(resetCode)

    private fun String.encode() = passwordEncoder.encode(this)

    private fun UserAccountPasswordForm.createUserAndInternalize() = UserAccountPassword(
            user = createUser(),
            password = password.encode()
    )

    private fun UserAccountOauthForm.createUserAndInternalize() = UserAccountOauth(
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
