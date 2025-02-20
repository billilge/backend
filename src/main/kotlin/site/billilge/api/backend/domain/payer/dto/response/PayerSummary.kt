package site.billilge.api.backend.domain.payer.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.payer.entity.Payer

@Schema
data class PayerSummary(
    @field:Schema(description = "납부자 ID", example = "1")
    val payerId: Long,
    @field:Schema(description = "납부자 이름", example = "황수민")
    val name: String,
    @field:Schema(description = "납부자 학번", example = "20211234")
    val studentId: String,
    @field:Schema(description = "서비스 가입 여부", example = "true")
    val registered: Boolean
) {
    companion object {
        @JvmStatic
        fun from(payer: Payer): PayerSummary {
            val studentId = payer.studentId ?: "${payer.enrollmentYear}XXXX"

            return PayerSummary(
                payerId = payer.id!!,
                name = payer.name,
                studentId = studentId,
                registered = payer.registered
            )
        }
    }
}
