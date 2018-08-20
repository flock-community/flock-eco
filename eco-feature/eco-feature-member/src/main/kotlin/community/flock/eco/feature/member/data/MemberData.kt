package community.flock.eco.feature.member.data

import community.flock.eco.feature.member.model.Member
import community.flock.eco.feature.member.model.MemberGender
import community.flock.eco.feature.member.repositories.MemberRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class LoadData (private val memberRepository: MemberRepository): ApplicationRunner {

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
    }
}