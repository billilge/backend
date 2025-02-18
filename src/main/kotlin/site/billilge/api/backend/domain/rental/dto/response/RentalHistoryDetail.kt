package site.billilge.api.backend.domain.rental.dto.response

import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

data class RentalHistoryDetail(
    val rentalHistoryId: Long,
    val memberId: Long,
    val itemId: Long,
    val rentAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    val rentalStatus: RentalStatus
) {
    companion object {
        @JvmStatic
        fun from(rentalHistory: RentalHistory): RentalHistoryDetail {
            return RentalHistoryDetail(
                rentalHistoryId = rentalHistory.id!!,
                memberId = rentalHistory.member.id!!,
                itemId = rentalHistory.item.id!!,
                rentAt = rentalHistory.rentAt,
                returnedAt = rentalHistory.returnedAt,
                rentalStatus = rentalHistory.rentalStatus
            )
        }
    }
}