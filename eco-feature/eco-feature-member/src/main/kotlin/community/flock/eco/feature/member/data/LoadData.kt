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
        memberRepository.saveAll((1..1000).map {
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
        })

        memberGroupRepository.saveAll((1..10).map {
            MemberGroup(
                    code = "GROUP_$it",
                    name = "Group_$it"
            )
        })

        val field1 = MemberField(
                name = "field_text",
                label = "Field Text",
                type = MemberFieldType.TEXT
        )

        val field2a = MemberField(
                name = "field_checkbox",
                label = "Field Checkbox",
                type = MemberFieldType.CHECKBOX
        )

        val field2b = MemberField(
                name = "field_checkbox_disabled",
                label = "Field Checkbox",
                type = MemberFieldType.CHECKBOX,
                disabled = true
        )

        val field3 = MemberField(
                name = "field_single_select",
                label = "Field Single Select",
                type = MemberFieldType.SINGLE_SELECT,
                options = sortedSetOf("Option 1", "Option 2"))

        val field4 = MemberField(
                name = "field_multi_select",
                label = "Field Multi Select",
                type = MemberFieldType.MULTI_SELECT,
                options = sortedSetOf("Option 3", "Option 4", "Option 5"))

        memberFieldRepository.save(field1)
        memberFieldRepository.save(field2a)
        memberFieldRepository.save(field2b)
        memberFieldRepository.save(field3)
        memberFieldRepository.save(field4)

    }
}
