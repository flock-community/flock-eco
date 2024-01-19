package community.flock.eco.feature.workspace.repositories

import community.flock.eco.feature.workspace.model.Workspace
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WorkspaceRepository : PagingAndSortingRepository<Workspace, UUID> {
    fun findByHost(host: String): Workspace?

    fun findAllByUsersUserId(userId: String): Set<Workspace>
}
