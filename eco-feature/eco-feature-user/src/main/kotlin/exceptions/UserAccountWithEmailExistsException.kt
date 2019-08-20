package community.flock.eco.feature.user.exceptions

import community.flock.eco.feature.user.model.User
import java.lang.RuntimeException

class UserAccountWithEmailExistsException(user: User): RuntimeException("User with email '${user.email}' already exists") {
}
