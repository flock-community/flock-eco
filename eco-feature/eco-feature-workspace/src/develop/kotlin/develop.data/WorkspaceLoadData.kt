package community.flock.eco.feature.workspace.develop.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.workspace.model.Workspace
import community.flock.eco.feature.workspace.model.WorkspaceUserRole
import community.flock.eco.feature.workspace.repositories.WorkspaceRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class WorkspaceLoadData(
    private val workspaceRepository: WorkspaceRepository
) : LoadData<Workspace> {

    override fun load(n: Int): Iterable<Workspace> {
        return listOf(
            Workspace(
                id = UUID.randomUUID(),
                host = "localhost:8080",
                name = "Localhost 8080",
                users = setOf(
                    WorkspaceUserRole(
                        userId = "1",
                        role = "ADMIN"
                    ),
                    WorkspaceUserRole(
                        userId = "2",
                        role = "USER"
                    )
                ),
                variables = mapOf(
                    "PRIMARY_COLOR" to "#FFFFFF",
                    "SECONDARY_COLOR" to "#FFFFFF"
                )
            ),
            Workspace(
                id = UUID.randomUUID(),
                host = "localhost:3000",
                name = "Localhost 3000",
                users = setOf(
                    WorkspaceUserRole(
                        userId = "1",
                        role = "ADMIN"
                    ),
                    WorkspaceUserRole(
                        userId = "2",
                        role = "MANAGER"
                    ),
                    WorkspaceUserRole(
                        userId = "3",
                        role = "USER"
                    )
                ),
                variables = mapOf(
                    "PRIMARY_COLOR" to "#000000",
                    "SECONDARY_COLOR" to "#000000"
                )
            )
        )
            .save()
    }

    private fun List<Workspace>.save(): Iterable<Workspace> = workspaceRepository.saveAll(this)
}
