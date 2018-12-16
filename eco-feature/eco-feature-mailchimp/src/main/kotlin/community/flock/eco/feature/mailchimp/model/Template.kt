package community.flock.eco.feature.mailchimp.model

data class Template(
        val id: String,
        val name: String,
        val type: TemplateType
)

enum class TemplateType {
    USER, BASE, GALLERY
}