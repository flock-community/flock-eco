package community.flock.eco.feature.member.mapper

import community.flock.eco.feature.member.graphql.MemberFieldInput
import community.flock.eco.feature.member.graphql.MemberInput
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGender
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import org.springframework.stereotype.Component
import community.flock.eco.feature.member.graphql.Member as MemberGraphql
import community.flock.eco.feature.member.graphql.MemberGender as MemberGenderGraphql
import community.flock.eco.feature.member.graphql.MemberStatus as MemberStatusGraphql

@Component
class MemberGraphqlMapper(
    private val memberGroupRepository: MemberGroupRepository
) {

    fun consume(it: MemberInput) = Member(
        id = 0,
        firstName = it.firstName,
        infix = it.infix,
        surName = it.surName,
        email = it.email,
        phoneNumber = it.phoneNumber,
        street = it.street,
        houseNumber = it.houseNumber,
        houseNumberExtension = it.houseNumberExtension,
        postalCode = it.postalCode,
        city = it.postalCode,
        country = it.country,
        language = it.language,
        gender = it.gender.consume(),
        groups = it.groups.consume(),
        fields = it.fields.consume(),
        birthDate = it.birthDate,
        status = consume(it.status)
    )

    fun produce(it: Member) = MemberGraphql(
        id = it.uuid.toString(),
        firstName = it.firstName,
        infix = it.infix,
        surName = it.surName,
        email = it.email,
        phoneNumber = it.phoneNumber,
        street = it.street,
        houseNumber = it.houseNumber,
        houseNumberExtension = it.houseNumberExtension,
        postalCode = it.postalCode,
        city = it.city,
        country = it.country,
        language = it.language,
        gender = it.gender?.produce(),
        birthDate = it.birthDate,
        groups = it.groups.produce(),
        fields = it.fields.entries.map {
            community.flock.eco.feature.member.graphql.MemberField(
                key = it.key,
                value = it.value
            )
        },
        status = it.status.produce(),
        created = it.created
    )

    fun MemberGenderGraphql?.consume(): MemberGender = this?.name
        ?.let { MemberGender.valueOf(it) }
        ?: MemberGender.UNKNOWN

    fun List<MemberFieldInput>.consume() = this
        .fold(mapOf<String, String>()) { acc, cur -> acc + (cur.key to cur.value) }
        .toMutableMap()

    fun List<String>.consume() = this
        .map { it }
        .toSet()
        .let { memberGroupRepository.findByCodes(it) }
        .toMutableSet()

    fun consume(it: MemberStatusGraphql?) = it?.name
        ?.let { MemberStatus.valueOf(it) }
        ?: MemberStatus.NEW

    fun MemberGender.produce() = MemberGenderGraphql.valueOf(this.name)
    fun MemberStatus.produce() = MemberStatusGraphql.valueOf(this.name)
    fun Set<MemberGroup>.produce() = this.map {
        community.flock.eco.feature.member.graphql.MemberGroup(
            code = it.code,
            name = it.name
        )
    }
}
