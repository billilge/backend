package site.billilge.api.backend.domain.display.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.display.appender.DisplayPosterAppender
import site.billilge.api.backend.domain.display.entity.DisplayPoster
import site.billilge.api.backend.domain.display.reader.DisplayPosterReader
import site.billilge.api.backend.domain.display.remover.DisplayPosterRemover

@Service
@Transactional(readOnly = true)
class DisplayPosterService(
    private val displayPosterReader: DisplayPosterReader,
    private val displayPosterAppender: DisplayPosterAppender,
    private val displayPosterRemover: DisplayPosterRemover,
) {
    fun getAllPosters(): List<DisplayPoster> = displayPosterReader.readAll()

    fun getActivePosters(): List<DisplayPoster> = displayPosterReader.readAllActive()

    @Transactional
    fun addPoster(imageUrl: String, title: String) {
        displayPosterAppender.save(DisplayPoster(id = null, title = title, imageUrl = imageUrl))
    }

    @Transactional
    fun updatePoster(posterId: Long, imageUrl: String?, title: String) {
        val poster = displayPosterReader.read(posterId)
        displayPosterAppender.save(poster.copy(title = title, imageUrl = imageUrl ?: poster.imageUrl))
    }

    @Transactional
    fun deletePoster(posterId: Long) {
        displayPosterReader.read(posterId)
        displayPosterRemover.remove(posterId)
    }

    @Transactional
    fun activatePoster(posterId: Long) {
        val poster = displayPosterReader.read(posterId)
        displayPosterAppender.save(poster.copy(isActive = true))
    }

    @Transactional
    fun deactivatePoster(posterId: Long) {
        val poster = displayPosterReader.read(posterId)
        displayPosterAppender.save(poster.copy(isActive = false))
    }
}
