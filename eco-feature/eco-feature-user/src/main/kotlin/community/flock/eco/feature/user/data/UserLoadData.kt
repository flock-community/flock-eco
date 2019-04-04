package community.flock.eco.feature.user.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.stereotype.Component

@Component
class UserLoadData(
        private val userRepository: UserRepository
) : LoadData<User> {

    override fun load(n: Int): Iterable<User> = (1..n)
            .map { user(it) }
            .also { userRepository.saveAll(it) }

    private fun user(int: Int) = User(
            name = "name-$int",
            reference = "reference-$int",
            email = "email-$int"
    )

}
