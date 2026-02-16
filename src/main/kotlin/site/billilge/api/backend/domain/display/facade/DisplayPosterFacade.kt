package site.billilge.api.backend.domain.display.facade

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.display.dto.request.DisplayPosterRequest
import site.billilge.api.backend.domain.display.dto.response.DisplayPosterFindAllResponse
import site.billilge.api.backend.domain.display.dto.response.DisplayPosterFindAllResponse.DisplayPosterDetail
import site.billilge.api.backend.domain.display.service.DisplayPosterService
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.external.s3.S3Service
import java.util.*

@Component
class DisplayPosterFacade(
    private val displayPosterService: DisplayPosterService,
    private val s3Service: S3Service,
) {
    fun getAllPosters(): DisplayPosterFindAllResponse {
        val posters = displayPosterService.getAllPosters()
        return DisplayPosterFindAllResponse(
            posters.map { DisplayPosterDetail.from(it) }
        )
    }

    fun addPoster(image: MultipartFile, request: DisplayPosterRequest) {
        val imageUrl = s3Service.uploadImageFile(image, "posters/${UUID.randomUUID()}")
            ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)
        displayPosterService.addPoster(imageUrl, request.title)
    }

    fun updatePoster(posterId: Long, image: MultipartFile?, request: DisplayPosterRequest) {
        val imageUrl = if (image != null && !image.isEmpty) {
            s3Service.uploadImageFile(image, "posters/${UUID.randomUUID()}")
                ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)
        } else {
            null
        }
        displayPosterService.updatePoster(posterId, imageUrl, request.title)
    }

    fun deletePoster(posterId: Long) {
        displayPosterService.deletePoster(posterId)
    }

    fun activatePoster(posterId: Long) {
        displayPosterService.activatePoster(posterId)
    }

    fun deactivatePoster(posterId: Long) {
        displayPosterService.deactivatePoster(posterId)
    }
}
