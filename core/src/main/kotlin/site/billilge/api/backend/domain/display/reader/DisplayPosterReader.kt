package site.billilge.api.backend.domain.display.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.domain.display.entity.DisplayPoster
import site.billilge.api.backend.domain.display.exception.DisplayPosterErrorCode
import site.billilge.api.backend.domain.display.repository.DisplayPosterRepository

@Component
class DisplayPosterReader(private val displayPosterRepository: DisplayPosterRepository) {

    fun read(id: Long): DisplayPoster =
        displayPosterRepository.findById(id) ?: throw ApiException(DisplayPosterErrorCode.POSTER_NOT_FOUND)

    fun readAll(): List<DisplayPoster> = displayPosterRepository.findAll()

    fun readAllActive(): List<DisplayPoster> = displayPosterRepository.findByIsActiveTrue()
}
