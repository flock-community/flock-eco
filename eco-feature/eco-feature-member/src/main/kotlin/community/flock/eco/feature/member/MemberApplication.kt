package community.flock.eco.feature.member

import community.flock.eco.feature.member.data.MemberLoadData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct


@SpringBootApplication
@Import(MemberConfiguration::class, MemberLoadData::class)
class MemberApplication {

    @Autowired
    lateinit var memberLoadData: MemberLoadData

    @PostConstruct
    fun init() {
        memberLoadData.load()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(MemberApplication::class.java, *args)
}
