package community.flock.eco.feature.member.resolvers

import MemberGraphqlMapper
import community.flock.eco.feature.member.graphql.MemberFilter
import community.flock.eco.feature.member.services.MemberService
import community.flock.eco.feature.member.specifications.MemberSpecification
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import community.flock.eco.feature.member.graphql.Member as MemberGraphql
import community.flock.eco.feature.member.graphql.MemberStatus as MemberStatusGraphql
import community.flock.eco.feature.member.model.Member as MemberModel

@Component
class MemberQueryResolver(
        private val memberService: MemberService,
        private val memberGraphqlMapper: MemberGraphqlMapper) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findMemberById(id: Long): MemberGraphql? = memberService
            .findById(id)
            ?.produce()

    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun findAllMembers(
            filter: MemberFilter?,
            page: Int?,
            size: Int?,
            sort: String?,
            order: String?) = pageable(page, size, sort, order)
            .let { pageable ->
                val specification = filter.consume()
                memberService
                        .findAll(specification, pageable)
                        .map { it.produce() }
            }

    @PreAuthorize("hasAuthority('MemberAuthority.READ')")
    fun countMembers(filter: MemberFilter?) = memberService.count(filter.consume())

    fun MemberFilter?.consume() = MemberSpecification(
            search = this?.search ?: "",
            statuses = this?.statuses?.map { it.consume() }?.toSet() ?: setOf(),
            groups = this?.groups?.toSet() ?: setOf()
    )

    fun MemberModel.produce() = memberGraphqlMapper.produce(this)
    fun MemberStatusGraphql.consume() = memberGraphqlMapper.consume(this)

    fun pageable(page: Int?, size: Int?, sort: String?, order: String?) = PageRequest.of(
            page ?: 0,
            size ?: 10,
            sort
                    ?.let {
                        Sort.by(
                                Sort.Direction.fromOptionalString(order)
                                        .orElse(Sort.DEFAULT_DIRECTION),
                                sort)
                    }
                    ?: Sort.unsorted())
}
