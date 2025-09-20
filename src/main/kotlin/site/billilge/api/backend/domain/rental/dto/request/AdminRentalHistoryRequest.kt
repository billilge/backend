package site.billilge.api.backend.domain.rental.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "관리자 전용 대여기록 추가 DTO")
data class AdminRentalHistoryRequest(
    @field:Schema(description = "대여자 ID", example = "1")
    val memberId: Long,

    @field:Schema(description = "대여할 물품의 ID", example = "1")
    val itemId: Long,

    @field:Schema(description = "대여할 물품의 수량", example = "1")
    val count: Int,

    @field:Schema(description = "대여 시작 시간 정보")
    val rentalTime: RentalTime
) {
    @Schema(description = "대여 시작 시간 (시간 및 분)")
    data class RentalTime(
        @field:Schema(description = "대여 시작 시각의 시간(24시간 기준)", example = "13")
        val hour: Int,

        @field:Schema(description = "대여 시작 시각의 분", example = "0")
        val minute: Int
    )
}
