package community.flock.eco.feature.user

import community.flock.eco.feature.user.data.UserLoadData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@SpringBootApplication
@Import(UserConfiguration::class, UserLoadData::class)
class UserApplication{

    @Autowired
    lateinit var userLoadData: UserLoadData

    @PostConstruct
    fun init() {
        userLoadData.load()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(UserApplication::class.java, *args)
}
