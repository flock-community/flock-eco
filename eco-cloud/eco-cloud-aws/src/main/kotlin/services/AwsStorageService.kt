package community.flock.eco.cloud.aws.services

import community.flock.eco.core.services.StorageService
import exceptions.CannotConnectToObjectStore
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.io.File
import java.io.InputStream
import javax.annotation.PostConstruct

@Component
@ConditionalOnProperty("flock.eco.cloud.aws.enabled")
class AwsStorageService : StorageService {

    val logger = LoggerFactory.getLogger(AwsStorageService::class.java)

    lateinit var s3Client: S3Client

    @PostConstruct
    private fun init() {
        try {
            s3Client = S3Client.create()
        } catch (ex: Exception) {
            throw CannotConnectToObjectStore(ex)
        }
    }

    override fun hasObject(bucket: String, key: String): Boolean = try {
        val request = HeadObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()
        s3Client.headObject(request)
        true
    } catch (ex: NoSuchKeyException) {
        false
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun listObjects(bucket: String, prefix: String?): List<String> = try {
        val request = ListObjectsV2Request.builder()
            .bucket(bucket)
            .prefix(prefix)
            .maxKeys(100)
            .build()
        s3Client.listObjectsV2(request)
            .contents()
            .map { it.key() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun getObject(bucket: String, key: String): ByteArray? = try {
        val request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()
        s3Client.getObject(request, ResponseTransformer.toBytes())
            .asByteArray()
    } catch (ex: NoSuchKeyException) {
        null
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun putObject(bucket: String, key: String, file: File): StorageService.StorageObject = try {
        val request: PutObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()
        val body = RequestBody.fromFile(file)
        s3Client.putObject(request, body)
            .let { StorageService.StorageObject() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun putObject(bucket: String, key: String, file: MultipartFile): StorageService.StorageObject = try {
        putObject(bucket, key, file.inputStream, file.size)
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun putObject(bucket: String, key: String, input: InputStream, length: Long): StorageService.StorageObject = try {
        val request: PutObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()
        val body = RequestBody.fromInputStream(input, length)
        s3Client.putObject(request, body)
            .let { StorageService.StorageObject() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun initChunk(bucket: String, key: String, metadata: Map<String, String>?): StorageService.StorageMultipartObject = try {
        val request = CreateMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(key)
            .metadata(metadata)
            .build()
        s3Client.createMultipartUpload(request)
            .let {
                StorageService.StorageMultipartObject(
                    uploadId = it.uploadId()
                )
            }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun putChunk(bucket: String, key: String, uploadId: String, index: Int, file: MultipartFile): StorageService.StorageChuck = try {

        val uploadRequest = UploadPartRequest.builder()
            .bucket(bucket)
            .key(key)
            .uploadId(uploadId)
            .partNumber(index + 1)
            .build()
        val body = RequestBody.fromInputStream(file.inputStream, file.size)
        s3Client.uploadPart(uploadRequest, body)
            .let { StorageService.StorageChuck() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun putChunk(bucket: String, key: String, uploadId: String, index: Int, file: File): StorageService.StorageChuck = try {

        val uploadRequest = UploadPartRequest.builder()
            .bucket(bucket)
            .key(key)
            .uploadId(uploadId)
            .partNumber(index + 1)
            .build()
        val body = RequestBody.fromFile(file)
        s3Client.uploadPart(uploadRequest, body)
            .let { StorageService.StorageChuck() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun putChunk(bucket: String, key: String, uploadId: String, index: Int, length: Long, input: InputStream): StorageService.StorageChuck = try {

        val uploadRequest = UploadPartRequest.builder()
            .bucket(bucket)
            .key(key)
            .uploadId(uploadId)
            .partNumber(index + 1)
            .build()
        val body = RequestBody.fromInputStream(input, length)
        s3Client.uploadPart(uploadRequest, body)
            .let { StorageService.StorageChuck() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }

    override fun completeChunk(bucket: String, key: String, uploadId: String): StorageService.StorageObject = try {
        val requestParts = ListPartsRequest.builder()
            .bucket(bucket)
            .key(key)
            .uploadId(uploadId)
            .build()

        val list = s3Client.listParts(requestParts)

        val eTags = list.parts()
            .map { part ->
                CompletedPart.builder()
                    .partNumber(part.partNumber())
                    .eTag(part.eTag())
                    .build()
            }

        val completedMultipartUpload = CompletedMultipartUpload.builder()
            .parts(eTags)
            .build()

        val request = CompleteMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(key)
            .uploadId(uploadId)
            .multipartUpload(completedMultipartUpload)
            .build()

        s3Client.completeMultipartUpload(request)
            .let { StorageService.StorageObject() }
    } catch (ex: Exception) {
        throw CannotConnectToObjectStore(ex)
    }
}
