package community.flock.eco.core.services

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream

interface StorageService {

    class StorageFile()

    data class StorageMultipartFile(
            val uploadId:String
    )

    class StorageChuck

    fun uploadFile(bucket: String, key: String, file: File): StorageFile
    fun uploadFile(bucket: String, key: String, file: MultipartFile): StorageFile
    fun uploadFile(bucket: String, key: String, input: InputStream, length: Long): StorageFile

    fun initChunk(bucket: String, key: String, metadata: Map<String, String>? = null): StorageMultipartFile

    fun uploadChunk(bucket: String, key: String, uploadId: String, index: Int, file: MultipartFile): StorageChuck
    fun uploadChunk(bucket: String, key: String, uploadId: String, index: Int, file: File): StorageChuck
    fun uploadChunk(bucket: String, key: String, uploadId: String, index: Int, length: Long, input: InputStream): StorageChuck

    fun completeChunk(bucket: String, key: String, uploadId: String): StorageFile

}
