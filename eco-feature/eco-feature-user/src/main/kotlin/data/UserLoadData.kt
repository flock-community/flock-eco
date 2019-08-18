package community.flock.eco.feature.user.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import community.flock.eco.feature.user.services.UserAuthorityService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserLoadData(
        val userRepository: UserRepository,
        val userAuthorityService: UserAuthorityService,
        val passwordEncoder:PasswordEncoder
) : LoadData<User> {

    override fun load(n: Int): Iterable<User> = (1..n)
            .map { user(it) }
            .also { userRepository.saveAll(it) }

    private fun user(int: Int) = User(
            name = "name-$int",
            reference = "reference-$int",
            email = "email-$int@email-$int.xx",
            secret = passwordEncoder.encode(int.toString()),
            authorities = userAuthorityService.allAuthorities()
                    .map { it.toName() }
                    .toSet()
    )

}
