package community.flock.eco.feature.user

import community.flock.eco.feature.user.data.UserLoadData
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(UserConfiguration::class, UserLoadData::class)
class UserApplication(userLoadData: UserLoadData) {

    init {
        userLoadData.load()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(UserApplication::class.java, *args)
}
