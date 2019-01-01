package community.flock.eco.feature.mailchimp.model

data class MailchimpMember(
        val id:String? = null,
        val firstName:String? = null,
        val lastName:String? = null,
        val email:String,
        val status: MailchimpMemberStatus,
        val tags: Set<String> = setOf()
)
