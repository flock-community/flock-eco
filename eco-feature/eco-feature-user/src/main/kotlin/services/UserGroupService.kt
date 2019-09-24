package community.flock.eco.feature.user.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserGroup
import community.flock.eco.feature.user.repositories.UserGroupRepository
import org.springframework.stereotype.Service


@Service
class UserGroupService(
        private val userGroupRepository: UserGroupRepository
) {

    fun findByCode(code:String) = userGroupRepository
            .findByCode(code)
            .toNullable()

    fun create(name: String, users: Set<User>? = null): UserGroup {
        val userGroup = UserGroup(
                name = name
        )
        return userGroupRepository.save(userGroup)
    }

}
