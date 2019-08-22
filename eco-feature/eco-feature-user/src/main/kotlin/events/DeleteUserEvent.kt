package community.flock.eco.feature.user.events

import community.flock.eco.feature.user.model.User

data class DeleteUserEvent(override val entity: User) : UserEvent(entity)
