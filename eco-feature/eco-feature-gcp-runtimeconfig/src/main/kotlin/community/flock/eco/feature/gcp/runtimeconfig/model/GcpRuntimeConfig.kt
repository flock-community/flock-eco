package community.flock.eco.feature.gcp.runtimeconfig.model

data class GcpRuntimeConfig(
        val name: String,
        val description: String?
){
    val key: String
        get() = name.split("/").last()
}