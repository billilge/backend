package site.billilge.api.backend.domain.display.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.display.entity.DisplayPoster
import site.billilge.api.backend.domain.display.exception.DisplayPosterErrorCode
import site.billilge.api.backend.domain.display.repository.DisplayPosterRepository
import site.billilge.api.backend.global.exception.ApiException

@Service
@Transactional(readOnly = true)
class DisplayPosterService(
    private val displayPosterRepository: DisplayPosterRepository,
) {
    fun getAllPosters(): List<DisplayPoster> {
        return displayPosterRepository.findAll()
    }

    fun getActivePosters(): List<DisplayPoster> {
        return displayPosterRepository.findByIsActiveTrue()
    }

    @Transactional
    fun addPoster(imageUrl: String, title: String) {
        val poster = DisplayPoster(
            title = title,
            imageUrl = imageUrl,
        )
        displayPosterRepository.save(poster)
    }

    @Transactional
    fun updatePoster(posterId: Long, imageUrl: String?, title: String) {
        val poster = displayPosterRepository.findById(posterId)
            .orElseThrow { ApiException(DisplayPosterErrorCode.POSTER_NOT_FOUND) }

        poster.update(title, imageUrl)
    }

    @Transactional
    fun deletePoster(posterId: Long) {
        val poster = displayPosterRepository.findById(posterId)
            .orElseThrow { ApiException(DisplayPosterErrorCode.POSTER_NOT_FOUND) }

        displayPosterRepository.delete(poster)
    }

    @Transactional
    fun activatePoster(posterId: Long) {
        val poster = displayPosterRepository.findById(posterId)
            .orElseThrow { ApiException(DisplayPosterErrorCode.POSTER_NOT_FOUND) }

        poster.activate()
    }

    @Transactional
    fun deactivatePoster(posterId: Long) {
        val poster = displayPosterRepository.findById(posterId)
            .orElseThrow { ApiException(DisplayPosterErrorCode.POSTER_NOT_FOUND) }

        poster.deactivate()
    }
}
