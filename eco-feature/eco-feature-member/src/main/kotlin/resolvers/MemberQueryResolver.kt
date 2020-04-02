package community.flock.eco.feature.member.resolvers

import community.flock.eco.feature.member.services.MemberService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import community.flock.eco.feature.member.graphql.Member as MemberGraphql
import community.flock.eco.feature.member.graphql.MemberField as MemberFieldGraphql
import community.flock.eco.feature.member.graphql.MemberGender as MemberGenderGraphql
import community.flock.eco.feature.member.graphql.MemberGroup as MemberGroupGraphql
import community.flock.eco.feature.member.graphql.MemberStatus as MemberStatusGraphql
import community.flock.eco.feature.member.model.Member as MemberModel
import community.flock.eco.feature.member.model.MemberGender as MemberGenderModel
import community.flock.eco.feature.member.model.MemberGroup as MemberGroupModel
import community.flock.eco.feature.member.model.MemberStatus as MemberStatusModel

@Component
class MemberQueryResolver(
        private val memberService: MemberService) : GraphQLQueryResolver {

    fun getFindById(id: Long): MemberGraphql? = memberService
            .findById(id)
            ?.produce()

    fun getFindAllMembers(page: Int, size: Int?) = PageRequest.of(page, size?:10)
            .let { pageable ->
                memberService
                        .findAll(pageable)
                        .map { it.produce() }
            }

}

fun MemberModel.produce() = MemberGraphql(
        id = this.id.toString(),
        firstName = this.firstName,
        infix = this.infix,
        surName = this.surName,
        email = this.email,
        phoneNumber = this.phoneNumber,
        street = this.street,
        houseNumber = this.houseNumber,
        houseNumberExtension = this.houseNumberExtension,
        postalCode = this.postalCode,
        city = this.city,
        country = this.country,
        language = this.language,
        gender = this.gender?.produce(),
        birthDate = this.birthDate,
        groups = this.groups.produce(),
        fields = this.fields.entries.map {
            MemberFieldGraphql(
                    key = it.key,
                    value = it.value
            )
        },
        status = this.status.produce(),
        created = this.created)


private fun MemberGenderModel.produce() = MemberGenderGraphql.valueOf(this.name)
private fun MemberStatusModel.produce() = MemberStatusGraphql.valueOf(this.name)
private fun Set<MemberGroupModel>.produce() = this.map {
    MemberGroupGraphql(
            code = it.code,
            name = it.name
    )
}
