package community.flock.eco.feature.mailchimp.clients

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.mailchimp.model.*
import community.flock.eco.feature.payment.exceptions.FetchTemplateException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate


@Component
class MailchimpClient(
        val restTemplateBuilder: RestTemplateBuilder) {


    @Value("\${flock.eco.feature.mailchimp.apiKey}")
    private lateinit var apiKey: String

    @Value("\${flock.eco.feature.mailchimp.requestUrl}")
    private lateinit var requestUrl: String

    @Value("\${flock.eco.feature.mailchimp.listId}")
    private lateinit var listId: String

    private val headers: HttpHeaders
        get() {
            val headers = HttpHeaders()
            headers.add("Content-Type", "application/json")
            return headers
        }

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
            throw FetchTemplateException(ex.responseBodyAsString)
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
            throw FetchTemplateException(ex.responseBodyAsString)
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
                    .map {
                        MailchimpMember(
                                id = it.get("id").asText(),
                                firstName = it.get("merge_fields").get("FNAME").asText(),
                                lastName = it.get("merge_fields").get("LNAME").asText(),
                                email = it.get("email_address").asText(),
                                status = it.get("status").asText()
                                        .let { MailchimpMemberStatus.valueOf(it.toUpperCase()) },
                                tags = it.get("tags").asIterable()
                                        .map { it.get("name").asText() }
                                        .toSet()
                        )

                    }
                    .let { PageImpl(it, page, total) }

        } catch (ex: HttpClientErrorException) {
            throw FetchTemplateException(ex.responseBodyAsString)
        }
    }

    fun postMembers(members: List<MailchimpMember>) {

        val mapper = ObjectMapper()

        try {
            val body = mapper.createObjectNode()
            body.put("update_existing", true)
            body.putArray("members")
                    .addAll(members.map {
                        val json = mapper.createObjectNode()
                        json.put("email_address", it.email)
                        json.put("status", it.status.toString().toLowerCase())
                        json.putPOJO("tags", setOf("Test"))
                        json.putObject("merge_fields")
                                .put("FNAME", it.firstName)
                                .put("LNAME", it.lastName)
                        json
                    })


            val entity = HttpEntity(body, headers)
            val res = restTemplate.exchange("$requestUrl/lists/$listId", HttpMethod.POST, entity, ObjectNode::class.java)
            println(res.body)
        } catch (ex: HttpClientErrorException) {
            throw FetchTemplateException(ex.responseBodyAsString)
        }
    }


}