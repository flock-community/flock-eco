package community.flock.eco.feature.workspace.model

import org.springframework.boot.web.server.MimeMappings
import org.springframework.http.MediaType
import java.io.File
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class WorkspaceImage(

    @Column(name = "image_name")
    val name: String,

    @Column(name = "image_file")
    val file: ByteArray

)

fun WorkspaceImage.getMediaType(): MediaType {
    val extension = File(name).extension
    val mime = MimeMappings.DEFAULT.get(extension)
    return MediaType.parseMediaType(mime)
}
