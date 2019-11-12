package community.flock.eco.feature.project.services

import community.flock.eco.feature.project.inputs.ProjectRoleInput
import community.flock.eco.feature.project.model.ProjectRole
import community.flock.eco.feature.project.repositories.ProjectRoleRepository
import org.springframework.stereotype.Service

@Service
class ProjectRoleService(
        val projectRoleRepository: ProjectRoleRepository) {

    fun create(input: ProjectRoleInput): ProjectRole = input
            .internalize()
            .run(projectRoleRepository::save)


    fun ProjectRoleInput.internalize(projectRole: ProjectRole? = null) = ProjectRole(
            name = this.name ?: projectRole?.name ?: throw RuntimeException("Name required")
    )

}
