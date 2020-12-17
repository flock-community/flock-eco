package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.forms.UserGroupForm
import community.flock.eco.feature.user.repositories.UserGroupRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest(classes = [UserConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userGroupService: UserGroupService

    @Autowired
    private lateinit var userGroupRepository: UserGroupRepository

    @Test
    fun `create new user`() {
        val form = UserForm(
                name = "User 1",
                email = "user-1@gmail.com"
        )
        val user = userService.create(form)
        assertNotNull(user)
    }


    @Test
    fun `remove user`() {
        val form = UserForm(
                name = "User 2",
                email = "user-1@gmail.com"
        )
        val user = userService.create(form)
        assertNotNull(user)

        userService.delete(user.code)

        assertEquals(0, userService.findAll().count())
    }

    @Test
    fun `remove user in group`() {
        val form = UserForm(
                name = "User 3",
                email = "user-3@gmail.com"
        )
        val user = userService.create(form)
        assertNotNull(user.id)

        val group = userGroupService.create(UserGroupForm("Test", setOf(user.code)))
        assertNotNull(group.id)

        userService.delete(user.code)

        assertEquals(0, userService.findAll().count())
        assertEquals(1, userGroupRepository.findAll().count())
    }

}
