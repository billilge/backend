package site.billilge.api.backend.domain.payer.dto.request

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class PayerRequest(
    @field:ArraySchema(schema = Schema(implementation = PayerItem::class))
    val payers: List<PayerItem>
) {
    @Schema
    data class PayerItem(
        @field:Schema(description = "납부자 이름", example = "황수민")
        val name: String,
        @field:Schema(description = "납부자 학번", example = "20211234")
        val studentId: String
    )
}
