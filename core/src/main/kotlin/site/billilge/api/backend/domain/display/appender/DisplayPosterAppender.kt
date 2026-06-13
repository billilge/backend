package site.billilge.api.backend.domain.display.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.display.entity.DisplayPoster
import site.billilge.api.backend.domain.display.repository.DisplayPosterRepository

@Component
class DisplayPosterAppender(private val displayPosterRepository: DisplayPosterRepository) {

    fun save(poster: DisplayPoster): DisplayPoster = displayPosterRepository.save(poster)
}
