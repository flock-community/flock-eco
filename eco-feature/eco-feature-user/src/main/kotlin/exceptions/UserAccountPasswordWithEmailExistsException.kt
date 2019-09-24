package community.flock.eco.feature.user.exceptions

import community.flock.eco.feature.user.model.UserAccount

class UserAccountPasswordWithEmailExistsException(userAccount: UserAccount) : EcoUserException("User with email '${userAccount.user.email}' already exists")
