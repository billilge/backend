package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.item.dto.response.ItemSummary
import site.billilge.api.backend.domain.member.dto.response.MemberSummary
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime


@Schema(description = "대여 이력 상세 정보")
data class RentalHistoryDetail(
    @field:Schema(description = "대여 이력 ID", example = "123")
    val rentalHistoryId: Long,
    @field:Schema(description = "대여한 회원의 요약 정보", required = true)
    val member: MemberSummary,
    @field:Schema(description = "대여한 물품의 요약 정보", required = true)
    val item: ItemSummary,
    @field:Schema(description = "대여 시작 시각")
    val rentAt: LocalDateTime,
    @field:Schema(description = "반납 시각 (반납되지 않았으면 null)")
    val returnedAt: LocalDateTime?,
    @field:Schema(description = "대여 상태", example = "PENDING")
    val rentalStatus: RentalStatus
)  {
    companion object {
        @JvmStatic
        fun from(rentalHistory: RentalHistory): RentalHistoryDetail {
            return RentalHistoryDetail(
                rentalHistoryId = rentalHistory.id!!,
                member = MemberSummary.from(rentalHistory.member),
                item = ItemSummary.from(rentalHistory.item),
                rentAt = rentalHistory.rentAt,
                returnedAt = rentalHistory.returnedAt,
                rentalStatus = rentalHistory.rentalStatus
            )
        }
    }
}