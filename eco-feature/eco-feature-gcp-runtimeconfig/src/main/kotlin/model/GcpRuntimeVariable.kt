package community.flock.eco.feature.gcp.runtimeconfig.model

data class GcpRuntimeVariable(
        val name: String,
        val updateTime: String?,
        val state: GcpRuntimeVariableState?,
        val value: String?,
        val text: String?
){
    val key: String
        get() = name.split("/").last()
}