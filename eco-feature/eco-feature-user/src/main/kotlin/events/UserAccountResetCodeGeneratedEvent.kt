package community.flock.eco.feature.user.events

import community.flock.eco.feature.user.model.UserAccountPassword

class UserAccountResetCodeGeneratedEvent(userAccountPassword: UserAccountPassword) : UserAccountEvent(userAccountPassword)
