package community.flock.eco.core.model

import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractCodeEntity(

    override val id: Long = 0,

    @Column(unique = true)
    open val code: String = UUID.randomUUID().toString()

) : AbstractIdEntity(id) {

    override fun hashCode(): Int {
        return Objects.hashCode(code)
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as AbstractCodeEntity
        return Objects.equals(code, other.code)
    }
}
