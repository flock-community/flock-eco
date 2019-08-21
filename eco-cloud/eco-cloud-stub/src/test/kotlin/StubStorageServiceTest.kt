package community.flock.eco.cloud.stub

import community.flock.eco.core.services.StorageService
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEvent
import org.springframework.context.event.EventListener
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.ResourceUtils

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [StubCloudConfiguration::class])
class StubStorageServiceTest {

    @Autowired
    lateinit var storageService: StorageService

    val bucket: String = "bucket-test"
    val key: String = "key-test"

    @Test
    fun `put file`() {
        val file = ResourceUtils.getFile("classpath:test.txt")
        storageService.putObject(bucket, key, file)
        val res = storageService.getObject(bucket, key)
        Assert.assertEquals("test\n", res?.let { String(it) })
    }

    @Test
    fun `put multipart`() {

        val file = ResourceUtils.getFile("classpath:test.txt")

        val uploadId = storageService.initChunk(bucket, key).uploadId

        storageService.putChunk(bucket, key, uploadId, 1, file)
        storageService.completeChunk(bucket, key, uploadId)

        val res = storageService.getObject(bucket, key)

        Assert.assertEquals("test\n", res?.let { String(it) })

    }
}
