package community.flock.eco.feature.user.exceptions

class UserAccountNotFoundForResetCodeException(resetCode: String) : EcoUserException("User account not found for reset code: $resetCode")
