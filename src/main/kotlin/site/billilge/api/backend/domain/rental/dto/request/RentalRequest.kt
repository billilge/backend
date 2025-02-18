package site.billilge.api.backend.domain.rental.dto.request

import java.time.LocalDateTime

data class RentalRequest(
    val itemId: Long,
    val count: Int,
    val rentalDate: LocalDateTime,
    val ignoreDuplicate: Boolean
)
