package community.flock.eco.feature.user.forms

import community.flock.eco.feature.user.model.UserAccountOauthProvider

abstract class UserAccountForm (
        open val email: String,
        open val name: String? = null,
        open val authorities: Set<String> = setOf())
