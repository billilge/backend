package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

@Schema
data class DashboardResponse(
    @field:ArraySchema(schema = Schema(implementation = RentalApplicationDetail::class))
    val applications: List<RentalApplicationDetail>
) {
    @Schema
    data class RentalApplicationDetail(
        @field:Schema(description = "대여 기록 ID", example = "123")
        val rentalHistoryId: Long,
        @field:Schema(description = "물품 이름", example = "우산")
        val itemName: String,
        @field:Schema(description = "물품 아이콘 URL", example = "https://www.example.com/test.svg")
        val itemImageUrl: String,
        @field:Schema(description = "물품 개수", example = "10")
        val rentedCount: Int,
        @field:Schema(description = "대여자 이름", example = "황수민")
        val renterName: String,
        @field:Schema(description = "대여자 학번", example = "20211234")
        val renterStudentId: String,
        @field:Schema(description = "대여 상태", example = "PENDING")
        val status: RentalStatus,
        @field:Schema(description = "신청일")
        val applicatedAt: LocalDateTime
    ) {
        companion object {
            @JvmStatic
            fun from(rentalHistory: RentalHistory): RentalApplicationDetail {
                return RentalApplicationDetail(
                    rentalHistoryId = rentalHistory.id!!,
                    itemName = rentalHistory.item.name,
                    itemImageUrl = rentalHistory.item.imageUrl,
                    rentedCount = rentalHistory.rentedCount,
                    renterName = rentalHistory.member.name,
                    renterStudentId = rentalHistory.member.studentId,
                    status = rentalHistory.rentalStatus,
                    applicatedAt = rentalHistory.applicatedAt
                )
            }
        }
    }
}
