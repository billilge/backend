package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.dto.response.MemberSummary
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.global.dto.PageableResponse
import java.time.LocalDateTime

@Schema
data class AdminRentalHistoryFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = AdminRentalHistoryDetail::class))
    val rentalHistories: List<AdminRentalHistoryDetail>,
    @field:Schema(description = "총 페이지 수", example = "1")
    override val totalPage: Int
) : PageableResponse {
    @Schema
    data class AdminRentalHistoryDetail(
        val rentalHistoryId: Long,
        val member: MemberSummary,
        val itemName: String,
        val rentAt: LocalDateTime,
        val returnedAt: LocalDateTime?,
        val rentalStatus: RentalStatus,
    ) {
        companion object {
            @JvmStatic
            fun from(rentalHistory: RentalHistory): AdminRentalHistoryDetail {
                return AdminRentalHistoryDetail(
                    rentalHistoryId = rentalHistory.id!!,
                    member = MemberSummary.from(rentalHistory.member),
                    itemName = rentalHistory.item.name,
                    rentAt = rentalHistory.rentAt,
                    returnedAt = rentalHistory.returnedAt,
                    rentalStatus = rentalHistory.rentalStatus,
                )
            }
        }
    }
}
