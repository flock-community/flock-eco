package community.flock.eco.feature.user.events

import community.flock.eco.feature.user.model.UserAccount

class UserAccountPasswordResetEvent(userAccount: UserAccount) : UserAccountEvent(userAccount)
