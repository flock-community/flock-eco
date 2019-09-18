package community.flock.eco.feature.user.exceptions

import community.flock.eco.feature.user.model.User

class UserAccountWithEmailExistsException(user: User) : EcoUserException("User with email '${user.email}' already exists")
