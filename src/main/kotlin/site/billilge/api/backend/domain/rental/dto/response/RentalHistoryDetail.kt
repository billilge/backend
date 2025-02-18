package site.billilge.api.backend.domain.rental.dto.response

import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

data class RentalHistoryDetail(
    val rentalHistoryId: Long,
    val memberId: Long,
    val itemId: Long,
    val rentAt: LocalDateTime,
    val returnedAt: LocalDateTime,
    val rentalStatus: RentalStatus
)
