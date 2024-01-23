package community.flock.eco.feature.workspace.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.workspace.graphql.kotlin.WorkspaceUserInput
import community.flock.eco.feature.workspace.mappers.WorkspaceGraphqlMapper
import community.flock.eco.feature.workspace.services.WorkspaceService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@Controller
@RequestMapping("/api/workspaces/{workspacesId}/users")
class WorkspaceUserController(
    private val workspaceGraphqlMapper: WorkspaceGraphqlMapper,
    private val workspaceService: WorkspaceService,
) {
    @GetMapping
    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun getAll(
        @PathVariable workspacesId: UUID,
    ) = workspaceService
        .findById(workspacesId)
        ?.let { workspaceGraphqlMapper.produce(it) }
        ?.let { workspace -> workspace.users }
        .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('WorkspaceAuthority.WRITE')")
    fun post(
        @PathVariable workspacesId: UUID,
        @RequestBody input: WorkspaceUserInput,
    ) = input
        .let { workspaceService.addWorkspaceUser(workspacesId, it.reference, it.role) }
        .let { workspaceGraphqlMapper.produce(it) }
        .toResponse()

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('WorkspaceAuthority.WRITE')")
    fun delete(
        @PathVariable workspacesId: UUID,
        @PathVariable userId: UUID,
    ) = workspaceService
        .findById(workspacesId)
        ?.users
        .toResponse()
}
