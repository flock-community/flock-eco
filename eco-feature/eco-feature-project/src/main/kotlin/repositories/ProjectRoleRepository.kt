package community.flock.eco.feature.project.repositories

import community.flock.eco.feature.project.model.Project
import community.flock.eco.feature.project.model.ProjectRole
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import repositories.CodeRepository

@Repository
interface ProjectRoleRepository : PagingAndSortingRepository<ProjectRole, Long>
