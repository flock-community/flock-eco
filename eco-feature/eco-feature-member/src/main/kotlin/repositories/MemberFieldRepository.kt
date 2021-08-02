package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.model.MemberField
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface MemberFieldRepository : CrudRepository<MemberField, Long> {

    fun findByName(name: String): Optional<MemberField>
}
