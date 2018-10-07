package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.model.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [UserConfiguration::class])
@AutoConfigureTestDatabase
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun testsSave() {
        userRepository.save(User(
                name = "member1",
                email = "member1@gmail.com",
                reference = "kdjhqif"
        ))

    }


}