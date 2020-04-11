package community.flock.eco.feature.user

import community.flock.eco.feature.user.develop.data.UserGroupLoadData
import community.flock.eco.feature.user.develop.data.UserLoadData
import community.flock.eco.feature.user.events.UserAccountResetCodeGeneratedEvent
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.context.event.EventListener

@SpringBootApplication(exclude = [
    SecurityAutoConfiguration::class])
@Import(UserConfiguration::class,
        UserLoadData::class,
        UserGroupLoadData::class)

class UserApplication(
        userLoadData: UserLoadData,
        userGroupLoadData: UserGroupLoadData) {

    init {
        userLoadData.load()
        userGroupLoadData.load()
    }

    @EventListener(UserAccountResetCodeGeneratedEvent::class)
    fun handleUserAccountPasswordResetEvent(ev: UserAccountResetCodeGeneratedEvent) {
        println("#/reset/${ev.entity.resetCode}")
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(UserApplication::class.java, *args)
}
