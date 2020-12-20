package community.flock.eco.cloud.stub.services

import community.flock.eco.core.model.MailMessage
import community.flock.eco.core.services.MailService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty("flock.eco.cloud.stub.enabled")
class StubMailService : MailService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun sendMail(message: MailMessage) {

        logger.warn("=== Send Mail ===")
        logger.warn(message.from.toString())
        logger.warn(message.recipients.toString())
        logger.warn(message.text)
        logger.warn("=================")
    }
}
