package community.flock.eco.feature.mailchimp.model

data class MailchimpTemplate(
    val id: String,
    val name: String,
    val type: MailchimpTemplateType
)

enum class MailchimpTemplateType {
    USER, BASE, GALLERY
}
