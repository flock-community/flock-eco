package community.flock.eco.feature.member.data

import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGender
import community.flock.eco.feature.member.model.MemberGroup
import community.flock.eco.feature.member.repositories.MemberGroupRepository
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class LoadData (
        private val memberRepository: MemberRepository,
        private val memberGroupRepository: MemberGroupRepository
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val members = 1.rangeTo(1000).map {
            Member(
                    firstName = "firstName-$it",
                    surName = "surName-$it",
                    gender = MemberGender.MALE,
                    email = "email-$it"
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
    }
}
