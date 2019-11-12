package community.flock.eco.feature.project.repositories

import community.flock.eco.feature.project.model.Project
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import repositories.CodeRepository

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, Long>, CodeRepository<Project> {

    @Query("SELECT p FROM Project p")
    fun findAllByUserRef(userRef:String): Iterable<Project>
}
