package community.flock.eco.feature.member.repositories

import community.flock.eco.feature.member.model.MemberGroup
import org.springframework.data.repository.CrudRepository

import org.springframework.stereotype.Service

@Service
interface MemberGroupRepository : CrudRepository<MemberGroup, Long> {
    fun deleteByCode(code: String)
}
