package community.flock.eco.feature.mailchimp.model

data class MailchimpMember(
        val id: String = "0",
        val webId: String = "0",
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val status: MailchimpMemberStatus = MailchimpMemberStatus.UNSUBSCRIBED,
        val tags: Set<String> = setOf(),
        val interests: Map<String, Boolean> = mapOf()
)
