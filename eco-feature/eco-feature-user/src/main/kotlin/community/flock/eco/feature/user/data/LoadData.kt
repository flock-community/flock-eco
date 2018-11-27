package community.flock.eco.feature.user.data

import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class LoadData(
        private val userRepository: UserRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        userRepository.saveAll((1..100).map {
            User(
                    name = "name-$it",
                    reference = "reference-$it",
                    email = "email-$it"
            )
        })

    }
}
