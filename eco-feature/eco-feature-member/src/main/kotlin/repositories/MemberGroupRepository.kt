package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.model.MemberGroup
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface MemberGroupRepository : CrudRepository<MemberGroup, Long> {

    fun findByCode(code: String): Optional<MemberGroup>

    @Query(
        "SELECT g " +
            "FROM MemberGroup g " +
            "WHERE g.code IN ?1"
    )
    fun findByCodes(code: Set<String>): Iterable<MemberGroup>
    fun deleteByCode(code: String)
}
