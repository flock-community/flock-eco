package community.flock.eco.feature.user.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.events.UserAccountPasswordResetEvent
import community.flock.eco.feature.user.events.UserAccountResetCodeGeneratedEvent
import community.flock.eco.feature.user.exceptions.UserAccountExistsException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserCode
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserEmail
import community.flock.eco.feature.user.forms.UserAccountForm
import community.flock.eco.feature.user.forms.UserAccountOauthForm
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserAccountOauth
import community.flock.eco.feature.user.model.UserAccountOauthProvider
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

    fun findUserAccountPasswordByUserCode(code: String) = userAccountPasswordRepository.findByUserCode(code).toNullable()
    fun findUserAccountPasswordByUserEmail(email: String) = userAccountPasswordRepository.findByUserEmailIgnoreCaseContaining(email).toNullable()
    fun findUserAccountPasswordByResetCode(resetCode: String) = userAccountPasswordRepository.findByResetCode(resetCode).toNullable()

    fun findUserAccountOauthByUserEmail(email: String, provider: UserAccountOauthProvider) = userAccountOauthRepository.findByUserEmailIgnoreCaseContainingAndProvider(email, provider).toNullable()
    fun findUserAccountOauthByReference(reference: String) = userAccountOauthRepository.findByReference(reference).toNullable()

    fun createUserAccountPassword(form: UserAccountPasswordForm): UserAccountPassword = findUserAccountPasswordByUserEmail(form.email)
            ?.let { throw UserAccountExistsException(it) }
            .let { form.internalize() }
            .let(userAccountRepository::save)

    fun createUserAccountOauth(form: UserAccountOauthForm): UserAccountOauth = findUserAccountOauthByUserEmail(form.email, form.provider)
            ?.let { throw UserAccountExistsException(it) }
            .let { form.internalize() }
            .let(userAccountRepository::save)

    fun createUserAccountPasswordWithoutPassword(userCode: String): UserAccountPassword? = findUserAccountPasswordByUserCode(userCode)
            ?.let { throw UserAccountExistsException(it) }
            .let { userService.findByCode(userCode) }
            ?.let { UserAccountPassword(user = it) }
            ?.let(userAccountRepository::save)

    fun generateResetCodeForUserEmail(email: String): String = findUserAccountPasswordByUserEmail(email)
            ?.copy(resetCode = UUID.randomUUID().toString())
            ?.let(userAccountRepository::save)
            ?.also { applicationEventPublisher.publishEvent(UserAccountResetCodeGeneratedEvent(it)) }
            ?.run { resetCode!! }
            ?: throw UserAccountNotFoundForUserEmail(email)

    fun generateResetCodeForUserCode(code: String): String = findUserAccountPasswordByUserCode(code)
            ?.copy(resetCode = UUID.randomUUID().toString())
            ?.let(userAccountRepository::save)
            ?.also { applicationEventPublisher.publishEvent(UserAccountResetCodeGeneratedEvent(it)) }
            ?.run { resetCode!! }
            ?: throw UserAccountNotFoundForUserCode(code)

    fun resetPasswordWithResetCode(resetCode: String, password: String) = findUserAccountPasswordByResetCode(resetCode)
            ?.copy(
                    secret = passwordEncoder.encode(password),
                    resetCode = null
            )
            ?.let(userAccountRepository::save)
            ?.let { applicationEventPublisher.publishEvent(UserAccountPasswordResetEvent(it)) }

    fun deleteByUserCode(code: String) = userAccountRepository.deleteByUserCode(code)

    private fun UserAccountPassword.generateResetCodeAndSave() = copy(resetCode = UUID.randomUUID().toString())
            .let(userAccountRepository::save)
            .let { applicationEventPublisher.publishEvent(UserAccountResetCodeGeneratedEvent(it)) }

    private fun UserAccountPasswordForm.internalize() = UserAccountPassword(
            user = userService.findByEmail(this.email) ?: this.createUser(),
            secret = passwordEncoder.encode(password)
    )

    private fun UserAccountOauthForm.internalize() = UserAccountOauth(
            user = userService.findByEmail(this.email) ?: this.createUser(),
            provider = provider,
            reference = reference
    )

    private fun UserAccountForm.createUser(): User = userService.create(UserForm(
            email = email,
            name = name,
            authorities = authorities
    ))
}
