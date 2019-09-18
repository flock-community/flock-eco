package community.flock.eco.feature.user.exceptions

class UserAccountNotFoundForUser(userCode: String) : EcoUserException("User account for user $userCode not found")
