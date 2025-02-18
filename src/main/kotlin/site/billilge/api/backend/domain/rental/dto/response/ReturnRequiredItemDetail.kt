package site.billilge.api.backend.domain.rental.dto.response

import site.billilge.api.backend.domain.item.dto.response.ItemSummary
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ReturnRequiredItemDetail(
    val item: ItemSummary,
    val rentalDayCount: Int
) {
    companion object {
        @JvmStatic
        fun from(rentalHistory: RentalHistory): ReturnRequiredItemDetail {
            val itemSummary = ItemSummary.from(rentalHistory.item)

            val daysCount = ChronoUnit.DAYS.between(rentalHistory.rentAt, LocalDateTime.now()).toInt()
            return ReturnRequiredItemDetail(
                item = itemSummary,
                rentalDayCount = daysCount
            )
        }
    }
}