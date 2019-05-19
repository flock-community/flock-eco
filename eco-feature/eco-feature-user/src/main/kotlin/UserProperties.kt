package community.flock.eco.feature.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "flock.eco.feature.user")
data class UserProperties(
        val secretType: SecretType = SecretType.NONE,
        val secretLength: Integer = Integer(8),
        val secretAlphabet: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",

        val secretResetMail:String
)

enum class SecretType {
    NONE, GENERATED, CHOSEN
}
