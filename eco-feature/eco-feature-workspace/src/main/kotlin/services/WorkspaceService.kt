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
        private val workspaceRepository: WorkspaceRepository) {

    fun findById(id: UUID) = workspaceRepository
            .findById(id)
            .toNullable()

    fun findAll(pageable: Pageable): Page<Workspace> = workspaceRepository
            .findAll(pageable)

    fun count() = workspaceRepository.count()

    fun findByHost(host: String) = workspaceRepository
            .findByHost(host)
            .toNullable()

    fun create(input: Workspace): Workspace = input
            .save()

    fun update(id: UUID, input: Workspace): Workspace = findById(id)
            ?.run { input.copy(id = id) }
            ?.save()
            ?: error("cannot update workspace")

    fun delete(id: UUID) = workspaceRepository
            .deleteById(id)

    fun addWorkspaceUserToWorkspace(workspaceId: UUID, ref: String) = findById(workspaceId)
            ?.let {
                val user = workspaceUserProvider.findWorkspaceUserByReference(ref)
                        ?: workspaceUserProvider.createWorkspaceUserByReference(ref)
                return it.copy(
                        users = it.users + WorkspaceUserRole(
                                id = user.id,
                                role = user.role
                        ))
            }
            ?.save()
            ?: error("cannot add user workspace")


    fun Workspace.save(): Workspace = try {
        workspaceRepository.save(this)
    } catch (e: Exception) {
        error("cannot save workspace")
    }
}
