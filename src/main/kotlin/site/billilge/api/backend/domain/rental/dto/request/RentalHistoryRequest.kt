package site.billilge.api.backend.domain.rental.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "대여 기록 생성 요청 DTO")
data class RentalHistoryRequest(
    @Schema(description = "대여할 물품의 ID", example = "1")
    val itemId: Long,

    @Schema(description = "대여할 물품의 수량", example = "1")
    val count: Int,

    @Schema(description = "대여 시작 시간 정보")
    val rentalTime: RentalTime,

    @Schema(description = "중복 대여 무시 여부 (true인 경우 중복 대여 진행)", example = "false")
    val ignoreDuplicate: Boolean
) {
    @Schema(description = "대여 시작 시간 (시간 및 분)")
    data class RentalTime(
        @Schema(description = "대여 시작 시각의 시간(24시간 기준)", example = "13")
        val hour: Int,

        @Schema(description = "대여 시작 시각의 분", example = "0")
        val minute: Int
    )
}