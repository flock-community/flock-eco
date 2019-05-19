package community.flock.eco.feature.user.services

import community.flock.eco.core.model.MailMessage
import community.flock.eco.core.services.CrudService
import community.flock.eco.core.services.MailService
import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.SecretType
import community.flock.eco.feature.user.UserProperties
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserSecretReset
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.repositories.UserSecretResetRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.LocalDateTime
import javax.mail.internet.InternetAddress


@Component
class UserService(
        val mailService: MailService,
        val passwordEncoder: PasswordEncoder,
        val userRepository: UserRepository,
        val userSecretResetRepository: UserSecretResetRepository,
        val userProperties: UserProperties
) : CrudService<User, String> {

    override fun create(item: User): User? = userRepository
            .save(item)

    override fun read(id: String): User? = userRepository
            .findByCode(id)
            .toNullable()

    override fun update(id: String, item: User): User? = userRepository
            .findByCode(id)
            .toNullable()
            ?.let {
                item.copy(
                        id = it.id,
                        code = it.code,
                        secret = it.secret,
                        updated = LocalDateTime.now()
                )
            }.let {
                userRepository.save(it)
            }


    override fun delete(id: String) = userRepository
            .findByCode(id)
            .map {
                userRepository.delete(it)
            }
            .toNullable()

    fun resetSecret(user: User): User {
        val password = generatePassword(userProperties.secretLength.toInt())
        return when (userProperties.secretType) {
            SecretType.NONE -> user
                    .copy(secret = null)
                    .let { userRepository.save(it) }
            SecretType.CHOSEN -> user
                    .also {
                        UserSecretReset(user = it).let {
                            userSecretResetRepository.save(it)
                        }
                    }

            SecretType.GENERATED -> user
                    .copy(
                            secret = password
                                    .let { passwordEncoder.encode(it) }
                    )
                    .let { userRepository.save(it) }
                    .also { sendSecret(password, it) }
        }
    }

    private fun generatePassword(length: Int): String {
        val random = SecureRandom()
        val alphabet = userProperties.secretAlphabet
        return (0..length)
                .map {
                    alphabet[random.nextInt(alphabet.length)]
                }
                .joinToString { "" }
    }

    private fun sendSecret(password: String, user: User) {
        MailMessage(
                from = InternetAddress(userProperties.secretResetMailFrom),
                recipients = listOf(
                        InternetAddress(user.email, user.name)
                ),
                subject = userProperties.secretResetMailSubject,
                text = String.format(userProperties.secretResetMessage, password)
        ).also {
            mailService.sendMail(it)
        }

    }


}
