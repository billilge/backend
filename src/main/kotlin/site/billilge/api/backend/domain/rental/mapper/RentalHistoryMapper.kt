package site.billilge.api.backend.domain.rental.mapper

import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryDetail
import site.billilge.api.backend.domain.rental.entity.RentalHistory

fun RentalHistory.toDto(): RentalHistoryDetail {
    return RentalHistoryDetail(
        rentalHistoryId = this.id!!,
        memberId = this.member.id!!,
        itemId = this.item.id!!,
        rentAt = this.rentAt,
        returnedAt = this.returnedAt,
        rentalStatus = this.rentalStatus
    )
}