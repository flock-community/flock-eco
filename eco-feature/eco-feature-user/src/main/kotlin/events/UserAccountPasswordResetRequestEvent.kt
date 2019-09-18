package community.flock.eco.feature.user.events

import community.flock.eco.feature.user.model.UserAccount

class UserAccountPasswordResetRequestEvent(userAccount: UserAccount) : UserAccountEvent(userAccount)
