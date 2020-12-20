package community.flock.eco.feature.workspace.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/workspace-roles")
class WorkspaceRoleController(
    private val workspaceUserProvider: WorkspaceUserProvider
) {

    @GetMapping
    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun getAll() = workspaceUserProvider
        .findRoles()
        .toResponse()
}
