package site.billilge.api.backend.domain.rental.mapper

import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryDetail
import site.billilge.api.backend.domain.rental.entity.RentalHistory

fun RentalHistory.toDto(): RentalHistoryDetail {
    return RentalHistoryDetail(
        rentalHistoryId = this.id ?: throw IllegalStateException("Rental ID cannot be null"),
        memberId = this.member.id ?: throw IllegalStateException("Member ID cannot be null"),
        itemId = this.item.id ?: throw IllegalStateException("Item ID cannot be null"),
        rentAt = this.rentAt,
        returnedAt = this.returnedAt,
        rentalStatus = this.rentalStatus
    )
}