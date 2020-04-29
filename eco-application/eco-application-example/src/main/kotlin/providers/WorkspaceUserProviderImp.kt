package community.flock.eco.application.example.providers

import community.flock.eco.feature.workspace.providers.WorkspaceUser
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import org.springframework.stereotype.Component

@Component
class WorkspaceUserProviderImp() : WorkspaceUserProvider {

    override fun findWorkspaceUserByReference(ref: String): WorkspaceUser? = createUser(ref)

    override fun createWorkspaceUserByReference(ref: String): WorkspaceUser = createUser(ref)

    override fun findWorkspaceUser(ids: List<String>): Iterable<WorkspaceUser> = ids
            .map { createUser(it) }

    override fun findRoles(): List<String> = listOf("MANAGER", "ADMIN", "USER")

}

fun createUser(ref: String) = WorkspaceUser(
        id = ref,
        name = "Name $ref"
)
