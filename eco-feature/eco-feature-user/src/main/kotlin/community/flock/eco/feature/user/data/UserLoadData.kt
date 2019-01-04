package community.flock.eco.feature.user.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Component
class UserLoadData(
        private val userRepository: UserRepository
) : LoadData<User> {

    override fun load(): Iterable<User> {
       val users = (1..100)
                .map {
                    User(
                            name = "name-$it",
                            reference = "reference-$it",
                            email = "email-$it"
                    )
                }
                .let { userRepository.saveAll(it) }
        return users

    }
}
