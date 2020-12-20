package community.flock.eco.feature.mailchimp.model

data class MailchimpInterestCategory(
    val id: String = "0",
    val title: String,
    val type: MailchimpInterestCategoryType
)

enum class MailchimpInterestCategoryType {
    CHECKBOXES, DROPDOWN, RADIO, HIDDEN
}
