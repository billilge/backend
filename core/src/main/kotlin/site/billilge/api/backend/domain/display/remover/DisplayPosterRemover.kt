package site.billilge.api.backend.domain.display.remover

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.display.repository.DisplayPosterRepository

@Component
class DisplayPosterRemover(private val displayPosterRepository: DisplayPosterRepository) {

    fun remove(id: Long) = displayPosterRepository.delete(id)
}
