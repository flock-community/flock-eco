package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.forms.UserForm
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
class UserGroupServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userGroupService: UserGroupService

    @Autowired
    private lateinit var userGroupRepository: UserGroupRepository

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
        Assert.assertNotNull(userGroupService.findByCode(group.code))

        Assert.assertEquals(0, userService.findAll().count())
        Assert.assertEquals(1, userGroupRepository.findAll().count())
    }
}
