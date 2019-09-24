package community.flock.eco.feature.user.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.events.UserCreateEvent
import community.flock.eco.feature.user.events.UserDeleteEvent
import community.flock.eco.feature.user.events.UserUpdateEvent
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.repositories.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service


@Service
class UserService(
        val userRepository: UserRepository,
        val applicationEventPublisher: ApplicationEventPublisher
) {

    fun findByCode(code: String) = userRepository
            .findByCode(code)
            .toNullable()

    fun create(form: UserForm): User = form
            .toUser()
            .let { userRepository.save(it) }
            .also { applicationEventPublisher.publishEvent(UserCreateEvent(it)) }

    fun read(code: String): User? = userRepository
            .findByCode(code)
            .toNullable()

    fun update(code: String, form: UserForm): User? = read(code)
            ?.let { it.merge(form) }
            ?.let { userRepository.save(it) }
            ?.also { applicationEventPublisher.publishEvent(UserUpdateEvent(it)) }


    fun delete(code: String): Unit = read(code)
            ?.let {
                userRepository.delete(it)
                applicationEventPublisher.publishEvent(UserDeleteEvent(it))
            }
            ?: Unit

    fun findByEmail(email: String) = userRepository.findByEmail(email)
            .toNullable()

    private fun UserForm.toUser() = User(
            name = this.name,
            email = this.email,
            authorities = this.authorities
    )

    private fun User.merge(form: UserForm) = this.copy(
            name = form.name,
            email = form.email,
            authorities = form.authorities
    )
}
