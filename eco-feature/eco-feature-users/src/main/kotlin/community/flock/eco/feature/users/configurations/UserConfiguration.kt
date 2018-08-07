package community.flock.eco.feature.users.configurations

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["community.flock.eco.feature.users"])
@ComponentScan(basePackages = ["community.flock.eco.feature.users"])
@EntityScan("community.flock.eco.feature.users.*")
class UserConfiguration {
}