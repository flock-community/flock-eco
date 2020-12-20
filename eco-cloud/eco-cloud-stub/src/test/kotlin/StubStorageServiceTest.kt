package community.flock.eco.cloud.stub

import community.flock.eco.core.services.StorageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ResourceUtils

@SpringBootTest(classes = [StubCloudConfiguration::class])
class StubStorageServiceTest(
    @Autowired private val storageService: StorageService
) {

    val bucket: String = "bucket-test"
    val key: String = "key-test"

    @Test
    fun `put file`() {
        val file = ResourceUtils.getFile("classpath:test.txt")
        storageService.putObject(bucket, key, file)
        val res = storageService.getObject(bucket, key)
        assertEquals("test\n", res?.let { String(it) })
    }

    @Test
    fun `put multipart`() {

        val file = ResourceUtils.getFile("classpath:test.txt")

        val uploadId = storageService.initChunk(bucket, key).uploadId

        storageService.putChunk(bucket, key, uploadId, 1, file)
        storageService.completeChunk(bucket, key, uploadId)

        val res = storageService.getObject(bucket, key)

        assertEquals("test\n", res?.let { String(it) })
    }
}
