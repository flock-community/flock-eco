package community.flock.eco.feature.workspace

import community.flock.eco.core.configurations.GraphqlConfiguration
import community.flock.eco.feature.workspace.controllers.WorkspaceController
import community.flock.eco.feature.workspace.controllers.WorkspaceRoleController
import community.flock.eco.feature.workspace.controllers.WorkspaceUserController
import community.flock.eco.feature.workspace.mappers.WorkspaceGraphqlMapper
import community.flock.eco.feature.workspace.resolvers.WorkspaceQueryResolver
import community.flock.eco.feature.workspace.services.WorkspaceService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(
    GraphqlConfiguration::class,
    WorkspaceService::class,
    WorkspaceGraphqlMapper::class,
    WorkspaceController::class,
    WorkspaceUserController::class,
    WorkspaceRoleController::class,
    WorkspaceQueryResolver::class
)
class WorkspaceConfiguration
