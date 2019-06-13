package community.flock.eco.feature.mailchimp.model

data class MailchimpMember(
        val email:String,
        val firstName:String? = null,
        val lastName:String? = null,
        val status: MailchimpMemberStatus = MailchimpMemberStatus.UNSUBSCRIBED,
        val tags: Set<String> = setOf()
)
