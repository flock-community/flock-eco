package community.flock.eco.feature.member.specifications

import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.model.MemberStatus
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Join
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class MemberSpecification(
    private val search: String = "",
    private val statuses: Set<MemberStatus> = setOf(),
    private val groups: Set<String> = setOf(),
) : Specification<Member> {
    override fun toPredicate(
        root: Root<Member>,
        cq: CriteriaQuery<*>,
        cb: CriteriaBuilder,
    ): Predicate {
        val searchCriteria =
            if (search.isNotBlank()) {
                cb.or(
                    cb.like(cb.lower(root.get("firstName")), "%${search.lowercase()}%"),
                    cb.like(cb.lower(root.get("surName")), "%${search.lowercase()}%"),
                    cb.like(cb.lower(root.get("email")), "%${search.lowercase()}%"),
                )
            } else {
                cb.conjunction()
            }

        val statusCriteria =
            if (statuses.isNotEmpty()) {
                val path: Expression<MemberStatus> = root.get("status")
                val predicate = cb.`in`(path)
                statuses.forEach { predicate.value(it) }
                predicate
            } else {
                cb.conjunction()
            }

        val groupCriteria =
            if (groups.isNotEmpty()) {
                val join: Join<Member, MemberGroup> = root.join("groups")
                val path: Expression<String> = join.get("code")
                val predicate = cb.`in`(path)
                groups.forEach { predicate.value(it) }
                predicate
            } else {
                cb.conjunction()
            }

        return cb.and(searchCriteria, statusCriteria, groupCriteria)
    }
}
