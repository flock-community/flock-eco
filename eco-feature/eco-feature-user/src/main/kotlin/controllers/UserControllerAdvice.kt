package community.flock.eco.feature.user.controllers

import community.flock.eco.core.exceptions.FlockEcoException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserCode
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserEmail
import community.flock.eco.feature.user.exceptions.UserAccountPasswordWithEmailExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UserAccountPasswordWithEmailExistsException::class)
    fun handleUserAccountWithEmailExistsException(e: UserAccountPasswordWithEmailExistsException) = respond(e, CONFLICT)

    @ExceptionHandler(UserAccountNotFoundForUserCode::class)
    fun handleUserAccountNotFoundForUser(e: UserAccountNotFoundForUserCode) = respond(NO_CONTENT)

    @ExceptionHandler(UserAccountNotFoundForUserEmail::class)
    fun handleUserAccountNotFoundForUser(e: UserAccountNotFoundForUserEmail) = respond(NO_CONTENT)

    private fun respond(status: HttpStatus) = ResponseEntity<String>(status)
    private fun respond(e: FlockEcoException, status: HttpStatus) = ResponseEntity<String>(e.message, status)

}
