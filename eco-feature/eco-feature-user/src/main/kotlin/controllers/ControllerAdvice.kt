package community.flock.eco.feature.user.controllers

import community.flock.eco.core.exceptions.FlockEcoException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUser
import community.flock.eco.feature.user.exceptions.UserAccountWithEmailExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(UserAccountWithEmailExistsException::class)
    fun handleUserAccountWithEmailExistsException(e: UserAccountWithEmailExistsException) = respond(e, CONFLICT)

    @ExceptionHandler(UserAccountNotFoundForUser::class)
    fun handleUserAccountNotFoundForUser(e: UserAccountNotFoundForUser) = respond(e, NOT_FOUND)

    private fun respond(e: FlockEcoException, status: HttpStatus) = ResponseEntity<String>(e.message, status)

}
