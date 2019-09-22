package community.flock.eco.feature.user.repositories

import community.flock.eco.feature.user.model.UserGroup
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase
class UserGroupRepositoryTest {

    @Autowired
    lateinit var usergroupRepository: UserGroupRepository

    @Test
    fun `save userGroup via repository`() {

        val userGroup = UserGroup(
                name = "group name"
        )

        usergroupRepository.save(userGroup)

        val res1 = usergroupRepository.findAllByNameIgnoreCaseContaining("GROUP NAME", Pageable.unpaged())
        assertEquals(1, res1.totalElements)
        assertEquals("group name", res1.content[0].name)

    }
}
