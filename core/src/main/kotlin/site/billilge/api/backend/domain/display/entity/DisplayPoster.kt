package site.billilge.api.backend.domain.display.entity

import java.time.LocalDateTime

data class DisplayPoster(
    val id: Long?,
    val title: String,
    val imageUrl: String,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
