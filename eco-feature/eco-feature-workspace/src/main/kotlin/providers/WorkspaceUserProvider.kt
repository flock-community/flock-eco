package community.flock.eco.feature.workspace.providers

data class WorkspaceUser(
        val id: String,
        val name: String
)

interface WorkspaceUserProvider {
    fun findWorkspaceUserByReference(ref:String):WorkspaceUser?
    fun createWorkspaceUserByReference(ref:String):WorkspaceUser
    fun findWorkspaceUser(ids: List<String>): Iterable<WorkspaceUser>
    fun findRoles(): List<String>
}
