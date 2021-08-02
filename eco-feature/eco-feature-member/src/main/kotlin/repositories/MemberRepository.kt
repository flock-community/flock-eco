package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberStatus
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface MemberRepository : PagingAndSortingRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    fun findByUuid(uuid: UUID): Optional<Member>

    @Query(
        "SELECT m " +
            "FROM Member m " +
            "WHERE m.id IN ?1"
    )
    fun findByIds(ids: List<Long>): Iterable<Member>

    @Query(
        "SELECT m " +
            "FROM Member m " +
            "WHERE m.id IN ?1"
    )
    fun findByUuids(ids: List<UUID>): Iterable<Member>

    fun findAllByEmail(email: String): Iterable<Member>

    fun findByStatus(status: MemberStatus): Iterable<Member>
}
