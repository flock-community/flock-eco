package community.flock.eco.feature.mailchimp.controllers

import community.flock.eco.feature.mailchimp.clients.MailchimpClient
import community.flock.eco.feature.mailchimp.model.MailchimpCampaign
import community.flock.eco.feature.mailchimp.model.MailchimpMember
import community.flock.eco.feature.mailchimp.model.MailchimpTemplate
import community.flock.eco.feature.mailchimp.model.MailchimpTemplateType
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/mailchimp")
class MailchimpController(
        private val mailchimpClient: MailchimpClient
) {

    @GetMapping("/members")
    @PreAuthorize("hasAuthority('MailchimpMemberAuthority.READ')")
    fun members(page: Pageable): ResponseEntity<List<MailchimpMember>> = mailchimpClient.getMembers(page)
            .let {
                val headers = HttpHeaders()
                headers.set("x-page", page.pageNumber.toString())
                headers.set("x-total", it.totalElements.toString())
                return ResponseEntity(it.content.toList(), headers, HttpStatus.OK)
            }

    @GetMapping("/templates")
    @PreAuthorize("hasAuthority('MailchimpTemplateAuthority.READ')")
    fun templates(): List<MailchimpTemplate> = mailchimpClient.getTemplates()
            .filter { it.type == MailchimpTemplateType.USER }

    @GetMapping("/campaigns")
    @PreAuthorize("hasAuthority('MailchimpCampaignAuthority.READ')")
    fun campaigns(): List<MailchimpCampaign> = mailchimpClient.getCampaigns()


}
