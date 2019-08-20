package community.flock.eco.feature.user.forms

data class UserAccountPasswordForm (
        override val email: String,
        override val name: String?,
        val password: String
) : UserAccountForm(email, name)
