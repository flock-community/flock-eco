package community.flock.eco.cloud.stub.services

import community.flock.eco.core.services.StorageService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream

@Component
@ConditionalOnProperty("flock.eco.cloud.stub.enabled")
class StubStoreService : StorageService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val store: MutableMap<String, MutableMap<String, ByteArray>> = mutableMapOf()

    private fun put(bucket: String, key: String, file: ByteArray? = null): ByteArray? {
        return store.getOrDefault(bucket, mutableMapOf()).put(key, file ?: ByteArray(0))
                .also {
                    logger.info("Put file in bucket: $bucket with key: $key")
                }
    }

    fun get(bucket: String, key: String): ByteArray? = store[bucket]?.get(key)
            .also {
                logger.info("Get file from bucket: $bucket and key: $key")
            }


    override fun uploadFile(bucket: String, key: String, file: File): StorageService.StorageFile = put(bucket, key, file.readBytes())
            .let { StorageService.StorageFile() }


    override fun uploadFile(bucket: String, key: String, file: MultipartFile): StorageService.StorageFile = put(bucket, key, file.bytes)
            .let { StorageService.StorageFile() }

    override fun uploadFile(bucket: String, key: String, input: InputStream, length: Long): StorageService.StorageFile = put(bucket, key, input.readBytes())
            .let { StorageService.StorageFile() }

    override fun initChunk(bucket: String, key: String, metadata: Map<String, String>?): StorageService.StorageMultipartFile = put(bucket, key)
            .let { StorageService.StorageMultipartFile(uploadId = "0") }


    override fun uploadChunk(bucket: String, key: String, uploadId: String, index: Int, file: MultipartFile): StorageService.StorageChuck = get(bucket, key)
            ?.let {
                put(bucket, key, (it + file.bytes))
            }
            .let { StorageService.StorageChuck() }


    override fun uploadChunk(bucket: String, key: String, uploadId: String, index: Int, file: File): StorageService.StorageChuck = get(bucket, key)
            ?.let {
                put(bucket, key, (it + file.readBytes()))
            }
            .let { StorageService.StorageChuck() }

    override fun uploadChunk(bucket: String, key: String, uploadId: String, index: Int, length: Long, input: InputStream): StorageService.StorageChuck = get(bucket, key)
            ?.let {
                put(bucket, key, (it + input.readBytes()))
            }
            .let { StorageService.StorageChuck() }

    override fun completeChunk(bucket: String, key: String, uploadId: String): StorageService.StorageFile = StorageService.StorageFile()


}
