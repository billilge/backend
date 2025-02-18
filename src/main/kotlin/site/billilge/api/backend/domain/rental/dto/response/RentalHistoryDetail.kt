package site.billilge.api.backend.domain.rental.dto.response

import site.billilge.api.backend.domain.item.dto.response.ItemSummary
import site.billilge.api.backend.domain.member.dto.response.MemberSummary
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

data class RentalHistoryDetail(
    val rentalHistoryId: Long,
    val memberId: MemberSummary,
    val itemId: ItemSummary,
    val rentAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    val rentalStatus: RentalStatus
) {
    companion object {
        @JvmStatic
        fun from(rentalHistory: RentalHistory): RentalHistoryDetail {
            return RentalHistoryDetail(
                rentalHistoryId = rentalHistory.id!!,
                memberId = MemberSummary.from(rentalHistory.member),
                itemId = ItemSummary.from(rentalHistory.item),
                rentAt = rentalHistory.rentAt,
                returnedAt = rentalHistory.returnedAt,
                rentalStatus = rentalHistory.rentalStatus
            )
        }
    }
}