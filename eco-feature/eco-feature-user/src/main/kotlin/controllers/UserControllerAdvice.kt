package community.flock.eco.feature.user.controllers

import community.flock.eco.core.exceptions.FlockEcoException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForResetCodeException
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserCode
import community.flock.eco.feature.user.exceptions.UserAccountNotFoundForUserEmail
import community.flock.eco.feature.user.exceptions.UserAccountWithEmailExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UserAccountWithEmailExistsException::class)
    fun handleUserAccountWithEmailExistsException(e: UserAccountWithEmailExistsException) = respond(e, CONFLICT)

    @ExceptionHandler(UserAccountNotFoundForUserCode::class)
    fun handleUserAccountNotFoundForUser(e: UserAccountNotFoundForUserCode) = respond(NO_CONTENT)

    @ExceptionHandler(UserAccountNotFoundForUserEmail::class)
    fun handleUserAccountNotFoundForUser(e: UserAccountNotFoundForUserEmail) = respond(NO_CONTENT)

    @ExceptionHandler(UserAccountNotFoundForResetCodeException::class)
    fun handleUserAccountNotFoundForResetCodeException(e: UserAccountNotFoundForResetCodeException) = respond(e, NOT_FOUND)

    private fun respond(status: HttpStatus) = ResponseEntity<String>(status)
    private fun respond(e: FlockEcoException, status: HttpStatus) = ResponseEntity<String>(e.message, status)

}
