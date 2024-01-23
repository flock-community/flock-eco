package community.flock.eco.feature.workspace.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.workspace.graphql.kotlin.WorkspaceInput
import community.flock.eco.feature.workspace.mappers.WorkspaceGraphqlMapper
import community.flock.eco.feature.workspace.model.getMediaType
import community.flock.eco.feature.workspace.services.WorkspaceService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@Controller
@RequestMapping("/api/workspaces")
class WorkspaceController(
    private val workspaceGraphqlMapper: WorkspaceGraphqlMapper,
    private val workspaceService: WorkspaceService,
) {
    @GetMapping
    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun getAll(pageable: Pageable) =
        workspaceService
            .findAll(pageable)
            .map { workspaceGraphqlMapper.produce(it) }
            .toResponse()

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun getById(
        @PathVariable id: UUID,
    ) = workspaceService
        .findById(id)
        ?.let { workspaceGraphqlMapper.produce(it) }
        .toResponse()

    @PostMapping
    @PreAuthorize("hasAuthority('WorkspaceAuthority.WRITE')")
    fun post(
        @RequestBody input: WorkspaceInput,
    ) = input
        .let { workspaceGraphqlMapper.consume(it) }
        .let { workspaceService.create(it) }
        .let { workspaceGraphqlMapper.produce(it) }
        .toResponse()

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WorkspaceAuthority.WRITE')")
    fun put(
        @PathVariable id: UUID,
        @RequestBody input: WorkspaceInput,
    ) = workspaceService
        .findById(id)
        ?.let { workspaceGraphqlMapper.consume(input, it) }
        ?.let { workspaceService.update(id, it) }
        ?.let { workspaceGraphqlMapper.produce(it) }
        .toResponse()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WorkspaceAuthority.WRITE')")
    fun delete(
        @PathVariable id: UUID,
    ) = workspaceService
        .findById(id)
        ?.let { workspaceService.delete(id) }
        .toResponse()

    @GetMapping("/{id}/image/*")
    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun getImage(
        @PathVariable id: UUID,
        authentication: Authentication?,
    ) = workspaceService
        .findById(id)
        ?.image
        ?.run {
            ResponseEntity
                .ok()
                .contentType(getMediaType())
                .body(file)
        }
        ?: ResponseEntity.notFound()
}
