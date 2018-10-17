package community.flock.eco.feature.member

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

fun <T> Optional<T>.safeRespond(): ResponseEntity<T> = when (this.isPresent) {
    true -> ResponseEntity(this.get(), HttpStatus.OK)
    else -> ResponseEntity(HttpStatus.NOT_FOUND)
}
