package community.flock.eco.feature.workspace.providers

data class WorkspaceUser(
    val id: String,
    val name: String
)

interface WorkspaceUserProvider {
    fun findWorkspaceUsers(ref: String): WorkspaceUser?
    fun findWorkspaceUsers(refs: List<String>): Iterable<WorkspaceUser>
    fun findRoles(): List<String>
}
