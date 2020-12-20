package community.flock.eco.application.example

import community.flock.eco.feature.member.develop.data.MemberLoadData
import community.flock.eco.feature.user.develop.data.UserLoadData
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.services.UserAccountService
import community.flock.eco.feature.user.services.UserAuthorityService
import community.flock.eco.feature.workspace.develop.data.WorkspaceLoadData
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component

@Component
@Import(
    UserLoadData::class,
    MemberLoadData::class,
    WorkspaceLoadData::class
)
class ApplicationData(
    userAuthorityService: UserAuthorityService,
    userAccountService: UserAccountService,
    userLoadData: UserLoadData,
    memberLoadData: MemberLoadData,
    workspaceLoadData: WorkspaceLoadData
) {

    init {
        val all = userAuthorityService
            .allAuthorities()
            .map { it.toName() }
            .toSet()
        userAccountService
            .createUserAccountPassword(UserAccountPasswordForm(email = "test", password = "test", authorities = all))
        userLoadData.load(10)
        memberLoadData.load(100)
        workspaceLoadData.load()
    }
}
