package community.flock.eco.feature.workspace.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.workspace.model.Workspace
import community.flock.eco.feature.workspace.model.WorkspaceUserRole
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import community.flock.eco.feature.workspace.repositories.WorkspaceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class WorkspaceService(
    private val workspaceUserProvider: WorkspaceUserProvider,
    private val workspaceRepository: WorkspaceRepository
) {

    fun findById(id: UUID) = workspaceRepository
        .findById(id)
        .toNullable()

    fun findAll(pageable: Pageable): Page<Workspace> = workspaceRepository
        .findAll(pageable)

    fun count() = workspaceRepository.count()

    fun findByHost(host: String) = workspaceRepository
        .findByHost(host)

    fun findWorkspacesByUserId(userId: String): Set<Workspace> =
        workspaceRepository.findAllByUsersUserId(userId)

    fun create(input: Workspace): Workspace = input
        .save()

    fun update(id: UUID, input: Workspace): Workspace = findById(id)
        ?.run { input.copy(id = id) }
        ?.save()
        ?: error("Cannot update workspace: $id, workspace does not exist")

    fun delete(id: UUID) = workspaceRepository
        .deleteById(id)

    fun addWorkspaceUser(workspaceId: UUID, ref: String, role: String) = findById(workspaceId)
        ?.let {
            val user = workspaceUserProvider.findWorkspaceUsers(ref)
                ?: error("Cannot add user: $ref to workspace: $workspaceId, user does not exist")
            it.copy(
                users = it.users + WorkspaceUserRole(
                    userId = user.id,
                    role = role.toUpperCase()
                )
            )
        }
        ?.save()
        ?: error("Cannot add user to workspace: $workspaceId")

    fun Workspace.save(): Workspace = try {
        workspaceRepository.save(this)
    } catch (e: Exception) {
        error("Cannot save workspace")
    }
}
