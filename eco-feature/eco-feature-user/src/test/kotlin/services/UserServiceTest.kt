package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.forms.UserForm
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [UserConfiguration::class])
@DataJpaTest
@AutoConfigureTestDatabase
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userGroupService: UserGroupService

    @Test
    fun `create new user`() {
        val form = UserForm(
                name = "User 1",
                email = "user-1@gmail.com"
        )
        val user = userService.create(form)
        Assert.assertNotNull(user)
    }

    @Test
    fun `create new group`() {
        val group = userGroupService.create("Test")
        Assert.assertNotNull(group)
    }

    @Test
    fun `create new group with user`() {
        val form = UserForm(
                name = "User 2",
                email = "user-2@gmail.com"
        )
        val user = userService.create(form)
        val group = userGroupService.create("Test", setOf(user))
        Assert.assertNotNull(group)
    }


    @Test
    fun `remove user which is in group`() {
        val form = UserForm(
                name = "User 3",
                email = "user-3@gmail.com"
        )
        val user = userService.create(form)
        Assert.assertNotNull(user.id)
        val group = userGroupService.create("Test", setOf(user))
        Assert.assertNotNull(group.id)
        userService.delete(user.code)

        Assert.assertNull(userService.findByCode(user.code))
        Assert.assertNull(userGroupService.(user.code))
    }

}
