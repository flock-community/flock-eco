package community.flock.eco.feature.user

import community.flock.eco.feature.user.data.UserGroupLoadData
import community.flock.eco.feature.user.data.UserLoadData
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootApplication(exclude = [
    SecurityAutoConfiguration::class])
@Import(UserConfiguration::class,
        UserLoadData::class)
class UserApplication(
        userLoadData: UserLoadData,
        userGroupLoadData: UserGroupLoadData) {

    init {
        userLoadData.load()
        userGroupLoadData.load()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(UserApplication::class.java, *args)
}
