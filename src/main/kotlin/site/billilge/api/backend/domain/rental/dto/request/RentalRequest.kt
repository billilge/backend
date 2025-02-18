package site.billilge.api.backend.domain.rental.dto.request

import java.time.Instant

data class RentalRequest(
    val itemId: Long,
    val count: Int,
    val rentalDate: Instant,
    val ignoreDuplicate: Boolean
)
