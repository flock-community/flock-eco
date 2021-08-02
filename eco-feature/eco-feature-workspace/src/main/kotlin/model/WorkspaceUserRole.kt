package community.flock.eco.feature.workspace.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class WorkspaceUserRole(

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "user_role")
    val role: String
)
