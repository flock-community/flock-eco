package community.flock.eco.feature.project.services

import community.flock.eco.feature.project.ProjectConfiguration
import community.flock.eco.feature.project.inputs.ProjectRoleInput
import community.flock.eco.feature.project.model.Project
import community.flock.eco.feature.project.model.ProjectRole
import community.flock.eco.feature.project.model.ProjectUser
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [ProjectConfiguration::class])
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectServiceTest {

    @Autowired
    private lateinit var projectService: ProjectService

    @Autowired
    private lateinit var projectRoleService: ProjectRoleService

    data class User(
            val code: String,
            val name: String)

    data class Group(
            val code: String,
            val name: String,
            val users: Set<User>)

    private val users = listOf<User>(
            createUser(1),
            createUser(2),
            createUser(3),
            createUser(4),
            createUser(5)
    )

    private val groups = listOf<Group>(
            createGroup(1, setOf(users[0])),
            createGroup(2, setOf(users[0], users[1]))
    )

    private fun createRoles() = listOf<ProjectRole>(
            createProjectRole(name = "VIEWER"),
            createProjectRole(name = "DEVELOPER"),
            createProjectRole(name = "ADMIN")
    )

    @Test
    fun findAll() {

        val roles = createRoles()

        val project1 = createProject(1, mapOf(users[0] to roles[2]), mapOf())
        val project2 = createProject(2, mapOf(), mapOf(groups[0] to roles[1]))

        val list = projectService.findUserHasRole("user-1")
        println(list)
    }

    private fun createUser(i: Int) = User(code = "user-$i", name = "User $i")
    private fun createGroup(i: Int, users: Set<User>) = Group(code = "group-$i", name = "Group $i", users = users)
    private fun createProject(i: Int, users: Map<User, ProjectRole>, groups: Map<Group, ProjectRole>) = Project(code = "project-$i", name = "Group $i", users = users.toProjectUser(), groups = setOf())
    private fun createProjectRole(name: String) = ProjectRoleInput(name = "name").run(projectRoleService::create)

    private fun Map<User, ProjectRole>.toProjectUser() = this
            .map { ProjectUser(projectRole = it.value, userRef = it.key.code) }
            .toSet()
}
