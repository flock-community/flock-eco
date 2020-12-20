package community.flock.eco.cloud.aws.services

import community.flock.eco.core.model.MailMessage
import community.flock.eco.core.services.MailService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.*

@Component
@ConditionalOnProperty("flock.eco.cloud.aws.enabled")
class AwsMailService : MailService {

    private val logger = LoggerFactory.getLogger(javaClass)

    val client: SesClient = SesClient.create()

    override fun sendMail(message: MailMessage) {

        val request = SendEmailRequest.builder()
            .source(message.from.address)
            .destination(
                Destination.builder()
                    .toAddresses(message.recipients.map { it.address })
                    .build()
            )
            .message(
                Message.builder()
                    .subject(
                        Content.builder()
                            .data(message.subject)
                            .build()
                    )
                    .body(
                        Body.builder().text(
                            Content.builder()
                                .data(message.text)
                                .build()
                        )
                            .build()
                    )
                    .build()
            )
            .build()

        client.sendEmail(request)
    }
}
