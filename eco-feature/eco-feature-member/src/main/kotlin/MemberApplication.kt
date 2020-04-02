package community.flock.eco.feature.member

import community.flock.eco.feature.member.data.MemberLoadData
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(MemberConfiguration::class, MemberLoadData::class)
class MemberApplication(memberLoadData: MemberLoadData) {

    init {
        memberLoadData.load(999)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(MemberApplication::class.java, *args)
}


