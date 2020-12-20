package community.flock.eco.core.model

import java.io.Serializable
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractEntity<ID : Serializable>(
    @Id
    open val id: ID
) : Serializable {

    override fun hashCode(): Int {
        return 13
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as AbstractEntity<*>
        return id != 0L && id == other.id
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"
}
