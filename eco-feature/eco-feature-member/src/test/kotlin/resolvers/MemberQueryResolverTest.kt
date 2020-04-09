package community.flock.eco.feature.member.resolvers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.data.MemberLoadData
import community.flock.eco.feature.member.graphql.Member
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@RunWith(SpringRunner::class)
@ContextConfiguration(classes=[MemberConfiguration::class])
@GraphQLTest
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(MemberLoadData::class)
class MemberQueryResolverTest {

    @Autowired
    lateinit var graphQLTestTemplate: GraphQLTestTemplate

    @Autowired
    lateinit var memberLoadData: MemberLoadData

    @Autowired
    lateinit var mapper: ObjectMapper

    @Test
    fun countMembers() {
        memberLoadData.load(33)
        val res = graphQLTestTemplate.perform("count-member.graphql")
        assertNotNull(res);
        assertTrue(res.isOk);
        assertEquals("33", res.get("$.data.countMembers"));
    }

    @Test
    fun findAllMembers() {
        memberLoadData.load(33)
        val variables: ObjectNode = ObjectMapper().createObjectNode()
        variables.put("search", "1")
        val res = graphQLTestTemplate.perform("find-page.graphql", variables)
        assertNotNull(res);
        assertTrue(res.isOk);
        assertEquals("2", res.get("$.data.list.length()"));
        assertEquals("first-name-1", res.get("$.data.list[0].firstName"));
        assertEquals("13", res.get("$.data.count"));

    }
}
