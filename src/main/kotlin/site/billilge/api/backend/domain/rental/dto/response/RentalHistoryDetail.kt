package site.billilge.api.backend.domain.rental.dto.response

import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.Instant

data class RentalHistoryDetail(
    val rentalHistoryId: Long,
    val memberId: Long,
    val itemId: Long,
    val rentAt: Instant,
    val returnedAt: Instant?,
    val rentalStatus: RentalStatus
)
