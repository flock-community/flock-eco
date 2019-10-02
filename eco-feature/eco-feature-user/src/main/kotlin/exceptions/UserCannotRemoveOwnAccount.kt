package community.flock.eco.feature.user.exceptions

import community.flock.eco.feature.user.model.UserAccount

class UserCannotRemoveOwnAccount() : EcoUserException("Cannot remove your own user")
