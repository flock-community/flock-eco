package community.flock.eco.feature.workspace.resolvers

import community.flock.eco.core.graphql.Pageable
import community.flock.eco.feature.workspace.mappers.WorkspaceGraphqlMapper
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import community.flock.eco.feature.workspace.services.WorkspaceService
import extentions.consume
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import java.util.*
import javax.transaction.Transactional

@Component
@Transactional
class WorkspaceQueryResolver(
    private val workspaceUserProvider: WorkspaceUserProvider,
    private val workspaceGraphqlMapper: WorkspaceGraphqlMapper,
    private val workspaceService: WorkspaceService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun findWorkspaceAll(pageable: Pageable?) = workspaceService
        .findAll(pageable.consume())
        .map { workspaceGraphqlMapper.produce(it) }
        .toList()

    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun findWorkspaceById(id: UUID) = workspaceService
        .findById(id)
        ?.let { workspaceGraphqlMapper.produce(it) }

    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun countWorkspaceAll() = workspaceService
        .count()

    @PreAuthorize("hasAuthority('WorkspaceAuthority.READ')")
    fun findWorkspaceRolesAll() = workspaceUserProvider
        .findRoles()
}
