package community.flock.eco.feature.user.repositories

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.model.User
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `save user via repository`() {
        userRepository.save(User(
                name = "User Name",
                email = "user@gmail.com"
        ))

        val res = userRepository.findByEmail("user@gmail.com")
                .toNullable()

        assertEquals("User Name", res!!.name)

    }


}
