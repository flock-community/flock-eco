package community.flock.eco.feature.member.resolvers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.develop.data.MemberLoadData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.context.annotation.Import
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@GraphQLTest
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@AutoConfigureWebClient
@Import(MemberConfiguration::class, MemberLoadData::class)
class MemberQueryResolverTest(
    @Autowired private val graphQLTestTemplate: GraphQLTestTemplate,
    @Autowired private val memberLoadData: MemberLoadData
) {

    @Test
    fun countMembers() {
        memberLoadData.load(33)
        val res = graphQLTestTemplate.perform("count-member.graphql")
        assertNotNull(res)
        assertTrue(res.isOk)
        assertEquals("33", res.get("$.data.countMembers"))
    }

    @Test
    fun findAllMembers() {
        val variables: ObjectNode = ObjectMapper().createObjectNode()
        variables.put("search", "1")
        val res = graphQLTestTemplate.perform("find-page.graphql", variables)
        assertNotNull(res)
        assertTrue(res.isOk)
        assertEquals("2", res.get("$.data.list.length()"))
        assertEquals("first-name-1", res.get("$.data.list[0].firstName"))
        assertEquals("13", res.get("$.data.count"))
    }
}
