package community.flock.eco.core.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractIdEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        open val id: Long = 0
) {

    override fun hashCode(): Int {
        return 13
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as AbstractIdEntity
        return id != null && id.equals(other.id)
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

}