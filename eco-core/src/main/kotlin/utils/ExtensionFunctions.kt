package community.flock.eco.core.utils

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

fun <T> Page<T>.toResponse(): ResponseEntity<List<T>> = ResponseEntity(
        this.content.toList(),
        HttpHeaders().also {
            it.set("x-total", this.totalElements.toString())
        },
        HttpStatus.OK
)

@Deprecated("Page is already part of the request use Page<T>.toResponse()")
fun <T> Page<T>.toResponse(page: Pageable): ResponseEntity<List<T>> = ResponseEntity(
        this.content.toList(),
        HttpHeaders().also {
            it.set("x-page", page.pageNumber.toString())
            it.set("x-total", this.totalElements.toString())
        },
        HttpStatus.OK
)


fun <T> Optional<T>.toResponse(): ResponseEntity<T> = this
        .map { ResponseEntity.ok(it) }
        .orElseGet { ResponseEntity.notFound().build<T>() }

fun <T> T?.toResponse(): ResponseEntity<T> = if (this != null) {
    ResponseEntity.ok<T>(this)
} else {
    ResponseEntity.notFound().build<T>()
}

fun <T : Any> Optional<T>.toNullable(): T? = this
        .orElse(null)
