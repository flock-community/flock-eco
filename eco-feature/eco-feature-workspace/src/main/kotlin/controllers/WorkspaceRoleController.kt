package nl.probo.catalog.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.workspace.graphql.WorkspaceInput
import community.flock.eco.feature.workspace.mappers.WorkspaceGraphqlMapper
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import community.flock.eco.feature.workspace.services.WorkspaceService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/api/workspace-roles")
class WorkspaceRoleController(
        private val workspaceUserProvider: WorkspaceUserProvider) {

    @GetMapping
    fun getAll() = workspaceUserProvider
            .findRoles()
            .toResponse()
}



