package community.flock.eco.feature.project.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.project.inputs.ProjectInput
import community.flock.eco.feature.project.model.Project
import community.flock.eco.feature.project.repositories.ProjectRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProjectService(
        val projectRepository: ProjectRepository) {

    fun findAll(pageable: Pageable) = projectRepository
            .findAll(pageable)

    fun findUserHasRole(userRef:String) = projectRepository
            .findAllByUserRef(userRef)

    fun findUserHasRole(userRef:String, pageable: Pageable): Page<Project> = projectRepository
            .findAll(pageable)

    fun findByCode(code: String): Project? = projectRepository
            .findByCode(code).toNullable()

    fun create(input: ProjectInput): Project = input
            .internalize()
            .run(projectRepository::save)

    fun update(code: String, input: ProjectInput): Project = projectRepository
            .findByCode(code)
            .toNullable()
            .run { input.internalize(this) }
            .run(projectRepository::save)

    fun delete(code: String) = projectRepository
            .deleteByCode(code)

    fun ProjectInput.internalize(project: Project? = null) = Project(
            name = this.name ?: project?.name
    )

}
