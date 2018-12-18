package community.flock.eco.feature.mailchimp.controllers

import community.flock.eco.feature.mailchimp.clients.MailchimpClient
import community.flock.eco.feature.mailchimp.model.Campaign
import community.flock.eco.feature.mailchimp.model.Template
import community.flock.eco.feature.mailchimp.model.TemplateType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/mailchimp")
class MailchimpController(
        private val mailchimpClient: MailchimpClient
) {

    @GetMapping("/templates")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun templates(): List<Template> = mailchimpClient.fetchTemplates()
            .filter { it.type == TemplateType.USER }

    @GetMapping("/campaigns")
    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun campaigns(): List<Campaign> = mailchimpClient.fetchCampaigns()


}
