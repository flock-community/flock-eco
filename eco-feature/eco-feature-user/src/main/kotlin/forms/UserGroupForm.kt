package community.flock.eco.feature.user.forms

import community.flock.eco.feature.user.model.User

class UserGroupForm (
        val name: String? = "",
        val users: Set<String>? = setOf()
)
