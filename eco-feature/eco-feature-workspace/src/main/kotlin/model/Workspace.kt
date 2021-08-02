package community.flock.eco.feature.workspace.model

import community.flock.eco.core.model.AbstractEntity
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Workspace(

    @Id
    override val id: UUID = UUID.randomUUID(),

    val name: String,

    @Embedded
    val image: WorkspaceImage? = null,

    @ElementCollection
    val variables: Map<String, String?> = mapOf(),

    val host: String? = null,

    @ElementCollection
    val users: Set<WorkspaceUserRole> = setOf()

) : AbstractEntity<UUID>(id)
