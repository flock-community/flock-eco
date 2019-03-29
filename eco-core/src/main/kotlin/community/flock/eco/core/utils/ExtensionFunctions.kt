package community.flock.eco.core.utils

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> Page<T>.toResponse(page: Pageable): ResponseEntity<List<T>> = ResponseEntity(
        this.content.toList(),
        HttpHeaders().also {
            it.set("x-page", page.pageNumber.toString())
            it.set("x-total", this.totalElements.toString())
        },
        HttpStatus.OK
)
