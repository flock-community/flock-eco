package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.forms.UserGroupForm
import community.flock.eco.feature.user.repositories.UserGroupRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [UserConfiguration::class])
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        Assert.assertNotNull(user)
    }


    @Test
    fun `remove user`() {
        val form = UserForm(
                name = "User 2",
                email = "user-1@gmail.com"
        )
        val user = userService.create(form)
        Assert.assertNotNull(user)

        userService.delete(user.code)

        Assert.assertEquals(0, userService.findAll().count())
    }

    @Test
    fun `remove user in group`() {
        val form = UserForm(
                name = "User 3",
                email = "user-3@gmail.com"
        )
        val user = userService.create(form)
        Assert.assertNotNull(user.id)

        val group = userGroupService.create(UserGroupForm("Test", setOf(user.code)))
        Assert.assertNotNull(group.id)

        userService.delete(user.code)

        Assert.assertEquals(0, userService.findAll().count())
        Assert.assertEquals(1, userGroupRepository.findAll().count())
    }

}
