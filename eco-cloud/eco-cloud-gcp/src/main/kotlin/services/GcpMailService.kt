package community.flock.eco.cloud.gcp.services

import community.flock.eco.core.model.MailMessage
import community.flock.eco.core.services.MailService
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.MimeMessage

@Component
class GpcMailService : MailService {

    override fun sendMail(message: MailMessage) {

        val props = Properties()
        val session = Session.getDefaultInstance(props, null)

        try {
            val msg = MimeMessage(session)
            msg.setFrom(message.from)
            message.recipients.forEach {
                msg.addRecipient(Message.RecipientType.TO, it)
            }
            msg.subject = message.subject
            msg.setText(message.text)
            Transport.send(msg)
        } catch (e: AddressException) {
            // ...
        } catch (e: MessagingException) {
            // ...
        } catch (e: UnsupportedEncodingException) {
            // ...
        }
    }
}
