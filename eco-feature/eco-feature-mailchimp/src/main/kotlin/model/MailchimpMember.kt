package community.flock.eco.feature.mailchimp.model

data class MailchimpMember(
    val id: String = "0",
    val webId: String = "0",
    val email: String,
    val fields: Map<String, String?> = mapOf(),
    val status: MailchimpMemberStatus = MailchimpMemberStatus.UNSUBSCRIBED,
    val tags: Set<String> = setOf(),
    val interests: Map<String, Boolean> = mapOf(),
    val language: String? = null
)
