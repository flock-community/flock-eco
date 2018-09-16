package community.flock.eco.feature.member.data

import community.flock.eco.feature.member.model.*
import community.flock.eco.feature.member.repositories.MemberFieldRepository
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class LoadData(
        private val memberRepository: MemberRepository,
        private val memberGroupRepository: MemberGroupRepository,
        private val memberFieldRepository: MemberFieldRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val members = 1.rangeTo(1000).map {
            Member(
                    firstName = "firstName-$it",
                    surName = "surName-$it",
                    gender = MemberGender.MALE,
                    email = "email-$it",
                    fields = mapOf(
                            "field_text" to "123",
                            "field_checkbox" to "true",
                            "field_single_select" to "Option 1",
                            "field_multi_select" to "Option 3,Option 4")
            )
        }
        memberRepository.saveAll(members)

        val memberGroups = 1.rangeTo(10).map {
            MemberGroup(
                    code = "GROUP_$it",
                    name = "Group_$it"
            )
        }
        memberGroupRepository.saveAll(memberGroups)

        val field1 = MemberField(
                code = "FIELD_1",
                name = "field_text",
                label = "Field Text",
                type = MemberFieldType.TEXT
        )

        val field2 = MemberField(
                code = "FIELD_2",
                name = "field_checkbox",
                label = "Field Checkbox",
                type = MemberFieldType.CHECKBOX
        )

        val field3 = MemberField(
                code = "FIELD_3",
                name = "field_single_select",
                label = "Field Single Select",
                type = MemberFieldType.SINGLE_SELECT,
                options = sortedSetOf("Option 1","Option 2"))

        val field4 = MemberField(
                code = "FIELD_4",
                name = "field_multi_select",
                label = "Field Multi Select",
                type = MemberFieldType.MULTI_SELECT,
                options = sortedSetOf("Option 3","Option 4","Option 5"))

        memberFieldRepository.save(field1)
        memberFieldRepository.save(field2)
        memberFieldRepository.save(field3)
        memberFieldRepository.save(field4)

    }
}
