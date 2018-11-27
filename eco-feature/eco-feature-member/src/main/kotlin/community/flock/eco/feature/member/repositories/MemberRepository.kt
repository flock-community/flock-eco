package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.model.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

import org.springframework.stereotype.Service
import java.util.*

@Service
interface MemberRepository : PagingAndSortingRepository<Member, Long> {

    @Query("SELECT m FROM Member m JOIN m.groups g WHERE m.id = ?1")
    override fun findById(id: Long): Optional<Member>

    @Query("SELECT m FROM Member m WHERE m.id IN ?1")
    fun findByIds(ids: List<Long>): List<Member>

    @Query("SELECT m FROM Member m WHERE m.firstName LIKE %?1% OR m.surName LIKE %?1%")
    fun findByName(name: String): List<Member>

    fun findByEmail(email: String): List<Member>

    @Query("SELECT m FROM Member m WHERE m.firstName LIKE %?1% OR m.surName LIKE %?1% OR m.email LIKE %?1%")
    fun findBySearch(search: String): List<Member>

    @Query("SELECT m FROM Member m WHERE (m.firstName LIKE %?1% OR m.surName LIKE %?1% OR m.email LIKE %?1%) AND m.status != 'DELETED'")
    fun findBySearch(search: String, page: Pageable): Page<Member>

}
