package site.billilge.api.backend.domain.display.repository

import site.billilge.api.backend.domain.display.entity.DisplayPoster

interface DisplayPosterRepository {
    fun findById(id: Long): DisplayPoster?
    fun findAll(): List<DisplayPoster>
    fun save(poster: DisplayPoster): DisplayPoster
    fun delete(id: Long)
    fun findByIsActiveTrue(): List<DisplayPoster>
}
