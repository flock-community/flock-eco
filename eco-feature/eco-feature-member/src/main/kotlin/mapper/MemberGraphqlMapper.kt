import community.flock.eco.feature.member.events.MemberEvent
import community.flock.eco.feature.member.graphql.MemberFieldInput
import community.flock.eco.feature.member.graphql.MemberInput
import community.flock.eco.feature.member.graphql.Member as MemberGraphql
import community.flock.eco.feature.member.graphql.MemberField as MemberFieldGraphql
import community.flock.eco.feature.member.graphql.MemberGender as MemberGenderGraphql
import community.flock.eco.feature.member.graphql.MemberStatus as MemberStatusGraphql
import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGender
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.model.MemberStatus
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class MemberGraphqlMapper(
        private val memberGroupRepository: MemberGroupRepository
) {

    fun consume(it: MemberInput, member: Member? = null) = Member(
            id = member?.id ?: 0,
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
            gender = it.gender.consume(),
            groups = it.groups.consume(),
            fields = it.fields.consume(),
            birthDate = it.birthDate,
            status = consume(it.status))

    fun produce(it: Member) = community.flock.eco.feature.member.graphql.Member(
            id = it.id.toString(),
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
            created = it.created)

    fun MemberGenderGraphql?.consume(): MemberGender = this?.name
            ?.let { MemberGender.valueOf(it) }
            ?: MemberGender.UNKNOWN

    fun List<MemberFieldInput>.consume() = this
            .fold(mapOf<String, String>()) { acc, cur -> acc + (cur.key to cur.value) }

    fun List<String>.consume() = this
            .map { it }
            .toSet()
            .let { memberGroupRepository.findByCodes(it) }
            .toSet()

    fun consume(it: MemberStatusGraphql?) = it?.name
            ?.let { MemberStatus.valueOf(it) }
            ?: MemberStatus.NEW

    fun MemberGender.produce() = community.flock.eco.feature.member.graphql.MemberGender.valueOf(this.name)
    fun MemberStatus.produce() = community.flock.eco.feature.member.graphql.MemberStatus.valueOf(this.name)
    fun Set<MemberGroup>.produce() = this.map {
        community.flock.eco.feature.member.graphql.MemberGroup(
                code = it.code,
                name = it.name
        )
    }
}
