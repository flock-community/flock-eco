package community.flock.eco.feature.mailchimp.events

import community.flock.eco.core.events.Event
import java.time.LocalDateTime

data class MailchimpWebhookEvent(
    val type: MailchimpWebhookEventType,
    val firedAt: LocalDateTime,
    val id: String,
    val fields: Map<String, String> = mapOf(),
    val listId: String?,
    val email: String,
    val interests: Set<String>
) : Event

enum class MailchimpWebhookEventType {
    SUBSCRIBE,
    UNSUBSCRIBE,
    PROFILE,
    UPEMAIL,
    CLEANED,
    CAMPAIGN
}
