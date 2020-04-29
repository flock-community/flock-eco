package nl.probo.catalog.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.workspace.graphql.WorkspaceInput
import community.flock.eco.feature.workspace.graphql.WorkspaceUserInput
import community.flock.eco.feature.workspace.mappers.WorkspaceGraphqlMapper
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import community.flock.eco.feature.workspace.services.WorkspaceService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/api/workspaces/{workspacesId}/users")
class WorkspaceUserController(
        private val workspaceGraphqlMapper: WorkspaceGraphqlMapper,
        private val workspaceService: WorkspaceService) {

    @GetMapping
    fun getAll(@PathVariable workspacesId: UUID) = workspaceService
            .findById(workspacesId)
            ?.let { workspaceGraphqlMapper.produce(it) }
            ?.let { workspace -> workspace.users }
            .toResponse()

    @PostMapping
    fun post(@PathVariable workspacesId: UUID,
             @RequestBody input: WorkspaceUserInput) = input
            .let { workspaceService.addWorkspaceUser(workspacesId, it.reference, it.role) }
            .let { workspaceGraphqlMapper.produce(it) }
            .toResponse()

    @DeleteMapping("/{userId}")
    fun delete(
            @PathVariable workspacesId: UUID,
            @PathVariable userId: UUID) = workspaceService
            .findById(workspacesId)
            ?.users
            .toResponse()
}



