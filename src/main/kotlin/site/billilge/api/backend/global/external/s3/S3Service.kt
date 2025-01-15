package site.billilge.api.backend.global.external.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import java.io.IOException
import java.util.*

@Service
class S3Service(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}")
    val bucket: String,
    @Value("\${cloud.aws.s3.base-url}")
    val baseUrl: String
) {
    fun uploadImageFile(imageFile: MultipartFile, newFileName: String = "items/${UUID.randomUUID()}"): String? {
        val originalName = imageFile.originalFilename ?: return null

        val ext = originalName.substring(originalName.lastIndexOf("."))
        val changedName = newFileName + ext

        val metadata = ObjectMetadata().apply {
            contentType = "image/$ext"
            contentLength = imageFile.size
        }

        try {
            amazonS3.putObject(
                PutObjectRequest(bucket, changedName, imageFile.inputStream, metadata)
            )
        } catch (e: IOException) {
            throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED, e)
        }

        return amazonS3.getUrl(bucket, changedName).toString()
    }

    fun deleteImageFile(imageUrl: String) {
        val imageKey = imageUrl.replace(baseUrl, "")

        if (!amazonS3.doesObjectExist(bucket, imageKey)) {
            throw ApiException(GlobalErrorCode.IMAGE_NOT_FOUND)
        }

        try {
            amazonS3.deleteObject(bucket, imageKey)
        } catch (e: IOException) {
            throw ApiException(GlobalErrorCode.IMAGE_DELETE_FAILED, e)
        }
    }
}