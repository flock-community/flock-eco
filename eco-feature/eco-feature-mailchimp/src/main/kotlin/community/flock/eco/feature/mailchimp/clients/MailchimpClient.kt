package community.flock.eco.feature.mailchimp.clients

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.mailchimp.model.*
import community.flock.eco.feature.payment.exceptions.FetchException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


@Component
class MailchimpClient(
        val restTemplateBuilder: RestTemplateBuilder) {


    @Value("\${flock.eco.feature.mailchimp.apiKey:@null}")
    private lateinit var apiKey: String

    @Value("\${flock.eco.feature.mailchimp.requestUrl:@null}")
    private lateinit var requestUrl: String

    @Value("\${flock.eco.feature.mailchimp.listId:@null}")
    private lateinit var listId: String

    private val headers: HttpHeaders
        get() {
            val headers = HttpHeaders()
            headers.add("Content-Type", "application/json")
            return headers
        }

    val mapper = ObjectMapper()

    val restTemplate: RestTemplate
        get() {
            return restTemplateBuilder
                    .basicAuthorization("any", apiKey)
                    .build()
        }


    fun getTemplates(): List<MailchimpTemplate> {

        try {
            val entity = HttpEntity<String>(headers)
            val res = restTemplate.exchange("$requestUrl/templates", HttpMethod.GET, entity, ObjectNode::class.java)
            return res.body.get("templates").asIterable()
                    .map {
                        MailchimpTemplate(
                                id = it.get("id").asText(),
                                name = it.get("name").asText(),
                                type = it.get("type").asText().let {
                                    MailchimpTemplateType.valueOf(it.toUpperCase())
                                }
                        )
                    }
        } catch (ex: HttpClientErrorException) {
            throw FetchException(ex.responseBodyAsString)
        }
    }

    fun getCampaigns(): List<MailchimpCampaign> {

        try {
            val entity = HttpEntity<String>(headers)
            val res = restTemplate.exchange("$requestUrl/campaigns", HttpMethod.GET, entity, ObjectNode::class.java)
            return res.body.get("campaigns").asIterable()
                    .map {
                        MailchimpCampaign(
                                id = it.get("id").asText(),
                                webId = it.get("web_id").asText(),
                                name = it.get("settings").get("title").asText()
                        )
                    }
        } catch (ex: HttpClientErrorException) {
            throw FetchException(ex.responseBodyAsString)
        }
    }

    fun getMembers(page: Pageable): Page<MailchimpMember> {
        val offset = page.offset
        try {
            val entity = HttpEntity<String>(headers)
            val res = restTemplate.exchange("$requestUrl/lists/$listId/members?offset=$offset&count=10", HttpMethod.GET, entity, ObjectNode::class.java)
            fun printName(mergeFields: JsonNode): String {
                return mergeFields.get("FNAME").asText() + " " + mergeFields.get("LNAME").asText()
            }

            val total = res.body.get("total_items").asLong()
            return res.body.get("members").asIterable()
                    .map(this::convertObjectNodetoMailchimpMember)
                    .let { PageImpl(it, page, total) }

        } catch (ex: HttpClientErrorException) {
            throw FetchException(ex.responseBodyAsString)
        }
    }

    fun getMember(email: String): MailchimpMember? {
        val md5 = calculateMd5(email)
        val entity = HttpEntity<Unit>(headers)
        try {
            val res = restTemplate.exchange("$requestUrl/lists/$listId/members/$md5", HttpMethod.GET, entity, ObjectNode::class.java)
            return convertObjectNodetoMailchimpMember(res.body)
        } catch (ex: HttpClientErrorException) {
            return null
        }
    }

    fun postMember(member: MailchimpMember): MailchimpMember? {
        val obj = convertMailchimpMembertoObjectNode(member)
        val entity = HttpEntity(obj, headers)
        try {
            val res = restTemplate.exchange("$requestUrl/lists/$listId/members", HttpMethod.POST, entity, ObjectNode::class.java)
            return convertObjectNodetoMailchimpMember(res.body)
        } catch (ex: HttpClientErrorException) {
            return null
        }
    }

    fun putMember(member: MailchimpMember): MailchimpMember? {
        val md5 = calculateMd5(member.email)
        val obj = convertMailchimpMembertoObjectNode(member)
        val entity = HttpEntity(obj, headers)
        val res = restTemplate.exchange("$requestUrl/lists/$listId/members/$md5", HttpMethod.PUT, entity, ObjectNode::class.java)
        return if (res.statusCode == HttpStatus.OK) {
            convertObjectNodetoMailchimpMember(res.body)
        } else {
            null
        }
    }

    fun postMembers(members: List<MailchimpMember>) {
        try {
            val body = mapper.createObjectNode()
            body.put("update_existing", true)
            body.putArray("members")
                    .addAll(members
                            .map(this::convertMailchimpMembertoObjectNode))
            val entity = HttpEntity(body, headers)
            val res = restTemplate.exchange("$requestUrl/lists/$listId", HttpMethod.POST, entity, ObjectNode::class.java)
        } catch (ex: HttpClientErrorException) {
            throw FetchException(ex.responseBodyAsString)
        }
    }

    fun postSegment(tag: String): String {
        try {
            val body = mapper.createObjectNode()
            body.put("name", tag)
            body.putArray("static_segment")
            val entity = HttpEntity(body, headers)
            val res = restTemplate.exchange("$requestUrl/lists/$listId/segments", HttpMethod.POST, entity, ObjectNode::class.java)
            return res.body.get("name").asText()
        } catch (ex: HttpClientErrorException) {
            val res = mapper.readTree(ex.responseBodyAsString)
            val detail = res.get("detail").asText()
            return if (ex.statusCode == HttpStatus.BAD_REQUEST && detail == "Sorry, that tag already exists.") {
                tag
            } else {
                throw FetchException(ex.responseBodyAsString)
            }
        }
    }

    fun putTags(
            email: String,
            active: Set<String>,
            inactive: Set<String>) {
        val md5 = calculateMd5(email)
        val body = mapper.createObjectNode()
        val tags = body.putArray("tags")
        active.forEach {
            tags.add(mapper.createObjectNode()
                    .put("name", it)
                    .put("status", "active"))
        }
        inactive.forEach {
            tags.add(mapper.createObjectNode()
                    .put("name", it)
                    .put("status", "inactive"))
        }
        val entity = HttpEntity(body, headers)
        try {
            val res = restTemplate.exchange("$requestUrl/lists/$listId/members/$md5/tags", HttpMethod.POST, entity, ObjectNode::class.java)
        } catch (ex: HttpClientErrorException) {
            throw FetchException(ex.responseBodyAsString)
        }
    }


    fun calculateMd5(email: String): String {
        val digest = MessageDigest.getInstance("MD5")
                .digest(email
                        .toLowerCase()
                        .toByteArray())
        val md5 = DatatypeConverter.printHexBinary(digest)
        return md5.toLowerCase()
    }

    fun convertMailchimpMembertoObjectNode(member: MailchimpMember): ObjectNode {
        val json = mapper.createObjectNode()
        json.put("email_address", member.email)
        json.put("status", member.status.toString().toLowerCase())
        json.putPOJO("tags", member.tags)
        val merge = json.putObject("merge_fields")
        if (!member.firstName.isNullOrEmpty()) merge.put("FNAME", member.firstName)
        if (!member.lastName.isNullOrEmpty()) merge.put("LNAME", member.lastName)
        return json
    }

    fun convertObjectNodetoMailchimpMember(obj: JsonNode): MailchimpMember {
        return MailchimpMember(
                firstName = obj.get("merge_fields")
                        .get("FNAME")
                        .asText()
                        .let { if (it.isBlank()) null else it },
                lastName = obj.get("merge_fields")
                        .get("LNAME")
                        .asText()
                        .let { if (it.isBlank()) null else it },
                email = obj.get("email_address").asText(),
                status = obj.get("status").asText()
                        .let { MailchimpMemberStatus.valueOf(it.toUpperCase()) },
                tags = obj.get("tags").asIterable()
                        .map { it.get("name").asText() }
                        .toSet()
        )
    }

}