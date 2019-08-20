package community.flock.eco.feature.user.services

import community.flock.eco.core.services.MailService
import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.UserProperties
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


@Component
class UserService(
        val mailService: MailService,
        val passwordEncoder: PasswordEncoder,
        val userRepository: UserRepository,
        val userProperties: UserProperties
) {

    fun create(form: UserForm): User? = form
            .toUser()
            .let { userRepository.save(it) }

    fun read(code: String): User? = userRepository
            .findByCode(code)
            .toNullable()

    fun update(code: String, form: UserForm): User? = read(code)
            ?.let { it.merge(form) }
            ?.let { userRepository.save(it) }


    fun delete(idCode: String): Unit = read(idCode)
            ?.let { userRepository.delete(it) }
            ?: Unit

    fun findByEmail(email: String) = userRepository.findByEmail(email)
            .toNullable()

//    fun resetSecret(user: User): User {
//        val password = generatePassword(userProperties.secretLength.toInt())
//        return when (userProperties.secretType) {
//            SecretType.NONE -> user
//                    .copy(secret = null)
//                    .let { userRepository.save(it) }
//            SecretType.CHOSEN -> user
//                    .also {
//                        UserSecretReset(user = it).let {
//                            userSecretResetRepository.save(it)
//                        }
//                    }
//
//            SecretType.GENERATED -> user
//                    .copy(
//                            secret = password
//                                    .let { passwordEncoder.encode(it) }
//                    )
//                    .let { userRepository.save(it) }
//                    .also { sendSecret(password, it) }
//        }
//    }
//
//    private fun generatePassword(length: Int): String {
//        val random = SecureRandom()
//        val alphabet = userProperties.secretAlphabet
//        return (0..length)
//                .map {
//                    alphabet[random.nextInt(alphabet.length)]
//                }
//                .joinToString { "" }
//    }
//
//    private fun sendSecret(password: String, user: User) {
//        MailMessage(
//                from = InternetAddress(userProperties.secretResetMailFrom),
//                recipients = listOf(
//                        InternetAddress(user.email, user.name)
//                ),
//                subject = userProperties.secretResetMailSubject,
//                text = String.format(userProperties.secretResetMessage, password)
//        ).also {
//            mailService.sendMail(it)
//        }
//
//    }

    private fun UserForm.toUser() = User(
            name = this.name,
            email = this.email
    )

    private fun User.merge(form: UserForm) = this.copy(
            name = this.name,
            email = this.email
    )
}
