package community.flock.eco.cloud.aws.services

import community.flock.eco.core.model.MailMessage
import community.flock.eco.core.services.MailService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty


@Component
@ConditionalOnProperty("flock.eco.cloud.aws.enabled")
class AwsMailService : MailService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun sendMail(message: MailMessage) {

        logger.warn("=== Send Mail Aws ===")
        logger.warn(message.from.toString())
        logger.warn(message.recipients.toString())
        logger.warn(message.text)
        logger.warn("=================")

    }

}
