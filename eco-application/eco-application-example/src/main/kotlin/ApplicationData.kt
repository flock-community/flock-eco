package community.flock.eco.application.example

import community.flock.eco.feature.member.develop.data.MemberLoadData
import community.flock.eco.feature.user.develop.data.UserLoadData
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.services.UserAccountService
import community.flock.eco.feature.user.services.UserAuthorityService
import org.springframework.stereotype.Component


@Component
class ApplicationData(
        userAuthorityService: UserAuthorityService,
        userAccountService: UserAccountService,
        userLoadData: UserLoadData,
        memberLoadData: MemberLoadData) {

    init {
        val all = userAuthorityService
                .allAuthorities()
                .map { it.toName() }
                .toSet()
        userAccountService
                .createUserAccountPassword(UserAccountPasswordForm(email = "test", password = "test", authorities = all))
        userLoadData.load(10)
        memberLoadData.load(100)
    }
}




