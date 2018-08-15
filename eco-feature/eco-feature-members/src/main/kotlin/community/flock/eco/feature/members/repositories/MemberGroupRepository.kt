package community.flock.eco.feature.members.repositories

import community.flock.eco.feature.members.model.Member
import community.flock.eco.feature.members.model.MemberGroup
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

import org.springframework.stereotype.Service
import java.util.*


@Service
interface MemberGroupRepository : CrudRepository<MemberGroup, String> {

}


