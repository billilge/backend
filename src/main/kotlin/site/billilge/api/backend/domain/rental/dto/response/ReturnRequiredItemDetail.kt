package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.item.dto.response.ItemSummary
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Schema(description = "반납이 필요한 물품 상세 정보")
data class ReturnRequiredItemDetail(
    @field:Schema(description = "물품 요약 정보", required = true)
    val item: ItemSummary,
    @field:Schema(description = "대여 일수 (예: D+1에서의 1)", example = "1", required = true)
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