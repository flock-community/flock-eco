package community.flock.eco.application.example.providers

import community.flock.eco.feature.workspace.providers.WorkspaceUser
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import org.springframework.stereotype.Component

val user = WorkspaceUser(
        id = "123",
        name = "Name",
        role = "Role"
)

@Component
class WorkspaceUserProviderImp() : WorkspaceUserProvider {

    override fun findWorkspaceUserByReference(ref: String): WorkspaceUser? = user

    override fun createWorkspaceUserByReference(ref: String): WorkspaceUser = user

    override fun findWorkspaceUser(ids: List<String>): Iterable<WorkspaceUser> = listOf(
            user
    )

}
