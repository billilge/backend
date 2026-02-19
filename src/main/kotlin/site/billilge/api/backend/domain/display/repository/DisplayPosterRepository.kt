package site.billilge.api.backend.domain.display.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.display.entity.DisplayPoster

interface DisplayPosterRepository : JpaRepository<DisplayPoster, Long> {
    fun findByIsActiveTrue(): List<DisplayPoster>
}
