package community.flock.eco.feature.mailchimp

import community.flock.eco.feature.mailchimp.clients.MailchimpClient
import community.flock.eco.feature.mailchimp.controllers.MailchimpController
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(
    MailchimpClient::class,
    MailchimpController::class
)
class MailchimpConfiguration : WebMvcConfigurer
