package community.flock.eco.feature.mailchimp.controllers

import community.flock.eco.feature.mailchimp.clients.MailchimpClient
import community.flock.eco.feature.mailchimp.events.MailchimpWebhookEvent
import community.flock.eco.feature.mailchimp.events.MailchimpWebhookEventType
import community.flock.eco.feature.mailchimp.model.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/mailchimp")
class MailchimpController(
        private val mailchimpClient: MailchimpClient,
        private val publisher: ApplicationEventPublisher
) {

    @GetMapping("/members")
    @PreAuthorize("hasAuthority('MailchimpMemberAuthority.READ')")
    fun getMembers(page: Pageable): ResponseEntity<List<MailchimpMember>> = mailchimpClient.getMembers(page)
            .let {
                val headers = HttpHeaders()
                headers.set("x-page", page.pageNumber.toString())
                headers.set("x-total", it.totalElements.toString())
                return ResponseEntity(it.content.toList(), headers, HttpStatus.OK)
            }

    @GetMapping("/templates")
    @PreAuthorize("hasAuthority('MailchimpTemplateAuthority.READ')")
    fun getTemplates(): List<MailchimpTemplate> = mailchimpClient.getTemplates()
            .filter { it.type == MailchimpTemplateType.USER }

    @GetMapping("/interest-categories")
    @PreAuthorize("hasAuthority('MailchimpTemplateAuthority.READ')")
    fun getInterestCategories(): List<MailchimpInterestCategory> = mailchimpClient.getInterestsCategories()

    @GetMapping("/campaigns")
    @PreAuthorize("hasAuthority('MailchimpCampaignAuthority.READ')")
    fun getCampaigns(): List<MailchimpCampaign> = mailchimpClient.getCampaigns()

    @GetMapping("/webhook")
    fun getWebhook():ResponseEntity<*> = ResponseEntity<Any>(HttpStatus.OK)

    @PostMapping("/webhook", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun postWebhook(@RequestBody formData: MultiValueMap<String, String>) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val event = MailchimpWebhookEvent(
                type = formData.getFirst("type")
                        .let { it.toUpperCase() }
                        .let { MailchimpWebhookEventType.valueOf(it) },
                firedAt = formData.getFirst("fired_at")
                        .let { LocalDateTime.parse(it, formatter) },
                id = formData.getFirst("data[id]"),
                listId = formData.getFirst("data[list_id]"),
                email = formData.getFirst("data[email]")
        )
        publisher.publishEvent(event)
    }

}
