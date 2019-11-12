package repositories

import org.springframework.data.domain.Page
import java.util.*

interface CodeRepository<T> {

    fun findByCode(code:String): Optional<T>
    fun deleteByCode(code:String)
}
