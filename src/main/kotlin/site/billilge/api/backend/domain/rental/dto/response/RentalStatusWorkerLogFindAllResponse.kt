package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.dto.response.MemberSummary
import site.billilge.api.backend.domain.rental.entity.RentalStatusWorkerLog
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

@Schema
data class RentalStatusWorkerLogFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = RentalStatusWorkerLogDetail::class))
    val workers: List<RentalStatusWorkerLogDetail>
) {
    @Schema
    data class RentalStatusWorkerLogDetail(
        @field:Schema(description = "대여 상태", example = "CONFIRMED")
        val rentalStatus: RentalStatus,
        @field:Schema(description = "처리자 정보")
        val worker: MemberSummary?,
        @field:Schema(description = "상태 변경 시각")
        val createdAt: LocalDateTime,
    ) {
        companion object {
            @JvmStatic
            fun from(log: RentalStatusWorkerLog): RentalStatusWorkerLogDetail {
                return RentalStatusWorkerLogDetail(
                    rentalStatus = log.rentalStatus,
                    worker = log.worker?.let { MemberSummary.from(it) },
                    createdAt = log.createdAt,
                )
            }
        }
    }
}
