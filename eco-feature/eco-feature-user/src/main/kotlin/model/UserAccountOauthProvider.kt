package community.flock.eco.feature.user.model

import community.flock.eco.core.events.EventEntityListeners
import javax.persistence.Entity
import javax.persistence.EntityListeners

enum class UserAccountOauthProvider(name: String) {
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github")
}
