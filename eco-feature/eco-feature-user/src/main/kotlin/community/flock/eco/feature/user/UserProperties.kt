package community.flock.eco.feature.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "flock.eco.feature.user")
data class UserProperties(
        var secretType: SecretType = SecretType.NONE,
        var secretLength: Integer = Integer(8),
        var secretAlphabet: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
)

enum class SecretType {
    NONE, GENERATED, CHOSEN
}