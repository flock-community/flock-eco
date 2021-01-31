package community.flock.eco.feature.member.develop.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.member.model.*
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberLoadData(
    private val memberRepository: MemberRepository,
    private val memberGroupRepository: MemberGroupRepository,
    private val memberFieldRepository: MemberFieldRepository
) : LoadData<Member> {

    override fun load(n: Int): Iterable<Member> {

        val groups = (1..10)
            .map {
                MemberGroup(
                    code = "GROUP_$it",
                    name = "Group_$it"
                )
            }
            .let { memberGroupRepository.saveAll(it) }

        val fields = listOf(
            MemberField(
                name = "field_text",
                label = "Field Text",
                type = MemberFieldType.TEXT
            ),
            MemberField(
                name = "field_checkbox",
                label = "Field Checkbox",
                type = MemberFieldType.CHECKBOX
            ),
            MemberField(
                name = "field_single_select",
                label = "Field Single Select",
                type = MemberFieldType.SINGLE_SELECT,
                options = sortedSetOf("Option 1", "Option 2")
            ),
            MemberField(
                name = "field_multi_select",
                label = "Field Multi Select",
                type = MemberFieldType.MULTI_SELECT,
                options = sortedSetOf("Option 3", "Option 4", "Option 5")
            ),
            MemberField(
                name = "field_checkbox_disabled",
                label = "Field Checkbox",
                type = MemberFieldType.CHECKBOX,
                disabled = true
            )
        )
            .let { memberFieldRepository.saveAll(it) }

        val members = (1..n)
            .map {
                Member(
                    firstName = "first-name-$it",
                    surName = "sur-name-$it",
                    gender = MemberGender.MALE,
                    email = "$it@member.com",
                    groups = when (it % 4) {
                        0 -> mutableSetOf(groups.toList()[2], groups.toList()[3])
                        1 -> mutableSetOf(groups.toList()[2])
                        2 -> mutableSetOf(groups.toList()[2], groups.toList()[0])
                        3 -> mutableSetOf(groups.toList()[2], groups.toList()[1])
                        else -> mutableSetOf()
                    },
                    fields = mutableMapOf(
                        fields.toList()[0].name to "123",
                        fields.toList()[1].name to "true",
                        fields.toList()[2].name to "Option 1",
                        fields.toList()[3].name to "Option 3,Option 4"
                    ),
                    status = if (it % 2 == 0) MemberStatus.NEW else MemberStatus.ACTIVE
                )
            }.let { memberRepository.saveAll(it) }

        return members
    }
}
