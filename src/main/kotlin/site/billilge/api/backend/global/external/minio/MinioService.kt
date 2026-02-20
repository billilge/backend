package site.billilge.api.backend.global.external.minio

import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.StatObjectArgs
import io.minio.errors.ErrorResponseException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.external.FileStorageService
import java.io.IOException
import java.util.*

@Service
class MinioService(
    private val minioClient: MinioClient,
    @Value("\${minio.bucket}")
    private val bucket: String,
    @Value("\${minio.base-url}")
    private val baseUrl: String,
) : FileStorageService {

    override fun uploadImageFile(imageFile: MultipartFile, newFileName: String): String? {
        val originalName = imageFile.originalFilename ?: return null

        val ext = originalName.substring(originalName.lastIndexOf("."))
        val changedName = newFileName + ext

        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(changedName)
                    .stream(imageFile.inputStream, imageFile.size, -1)
                    .contentType(imageFile.contentType)
                    .build()
            )
        } catch (e: IOException) {
            throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED, e)
        }

        return "${baseUrl}/${bucket}/${changedName}"
    }

    override fun deleteImageFile(fileName: String) {
        val imageKey = fileName.replace("${baseUrl}/${bucket}/", "")

        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(imageKey)
                    .build()
            )
        } catch (e: ErrorResponseException) {
            throw ApiException(GlobalErrorCode.IMAGE_NOT_FOUND)
        }

        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(imageKey)
                    .build()
            )
        } catch (e: IOException) {
            throw ApiException(GlobalErrorCode.IMAGE_DELETE_FAILED, e)
        }
    }
}
