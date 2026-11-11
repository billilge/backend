package site.billilge.api.backend.global.external.r2

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.external.FileStorageService
import software.amazon.awssdk.core.exception.SdkException
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import java.io.IOException

@Service
class R2Service(
    private val r2Client: S3Client,
    @Value("\${r2.bucket}")
    private val bucket: String,
    @Value("\${r2.public-url}")
    publicUrl: String,
) : FileStorageService {

    // R2 공개 URL(r2.dev 또는 커스텀 도메인)은 버킷에 직접 연결되므로 경로에 버킷명이 들어가지 않는다
    private val publicUrl = publicUrl.trimEnd('/')

    override fun uploadImageFile(imageFile: MultipartFile, newFileName: String): String? {
        val originalName = imageFile.originalFilename ?: return null

        val ext = originalName.substring(originalName.lastIndexOf("."))
        val key = newFileName + ext

        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(imageFile.contentType)
            .build()

        try {
            imageFile.inputStream.use {
                r2Client.putObject(request, RequestBody.fromInputStream(it, imageFile.size))
            }
        } catch (e: IOException) {
            throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED, e)
        } catch (e: SdkException) {
            throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED, e)
        }

        return "$publicUrl/$key"
    }

    override fun deleteImageFile(fileName: String) {
        val key = fileName.removePrefix("$publicUrl/")

        try {
            r2Client.headObject(
                HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build()
            )
        } catch (e: S3Exception) {
            if (e.statusCode() == 404) throw ApiException(GlobalErrorCode.IMAGE_NOT_FOUND)
            throw ApiException(GlobalErrorCode.IMAGE_DELETE_FAILED, e)
        } catch (e: SdkException) {
            throw ApiException(GlobalErrorCode.IMAGE_DELETE_FAILED, e)
        }

        try {
            r2Client.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build()
            )
        } catch (e: SdkException) {
            throw ApiException(GlobalErrorCode.IMAGE_DELETE_FAILED, e)
        }
    }
}
