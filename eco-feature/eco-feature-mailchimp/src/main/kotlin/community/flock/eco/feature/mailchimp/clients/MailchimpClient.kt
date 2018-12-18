package community.flock.eco.feature.mailchimp.clients

import com.fasterxml.jackson.databind.node.ObjectNode
import community.flock.eco.feature.mailchimp.model.Campaign
import community.flock.eco.feature.mailchimp.model.Template
import community.flock.eco.feature.mailchimp.model.TemplateType
import community.flock.eco.feature.payment.exceptions.FetchTemplateException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException

@Component
class MailchimpClient(
        val restTemplateBuilder: RestTemplateBuilder) {


    @Value("\${flock.eco.feature.mailchimp.apiKey}")
    private lateinit var apiKey: String

    @Value("\${flock.eco.feature.mailchimp.requestUrl}")
    private lateinit var requestUrl: String


    private val headers: HttpHeaders
        get() {
            val headers = HttpHeaders()
            headers.add("Content-Type", "application/json")
            return headers
        }

    fun fetchTemplates(): List<Template> {

        val restTemplate = restTemplateBuilder
                .basicAuthorization("any", apiKey)
                .build()
        try {
            val entity = HttpEntity<String>(headers)
            val res = restTemplate.exchange("$requestUrl/templates", HttpMethod.GET, entity, ObjectNode::class.java)
            return res.body.get("templates").asIterable()
                    .map {
                        Template(
                                id = it.get("id").asText(),
                                name = it.get("name").asText(),
                                type = it.get("type").asText().let {
                                    TemplateType.valueOf(it.toUpperCase())
                                }
                        )
                    }
        } catch (ex: HttpClientErrorException) {
            throw FetchTemplateException(ex.responseBodyAsString)
        }
    }

    fun fetchCampaigns(): List<Campaign> {
        val restTemplate = restTemplateBuilder
                .basicAuthorization("any", apiKey)
                .build()
        try {
            val entity = HttpEntity<String>(headers)
            val res = restTemplate.exchange("$requestUrl/campaigns", HttpMethod.GET, entity, ObjectNode::class.java)
            return res.body.get("campaigns").asIterable()
                    .map {
                        Campaign(
                                id = it.get("id").asText(),
                                name = it.get("settings").get("title").asText()
                        )
                    }
        } catch (ex: HttpClientErrorException) {
            throw FetchTemplateException(ex.responseBodyAsString)
        }
    }
}