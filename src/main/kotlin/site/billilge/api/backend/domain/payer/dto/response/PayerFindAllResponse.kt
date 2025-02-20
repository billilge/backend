package site.billilge.api.backend.domain.payer.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.global.dto.PageableResponse

@Schema
data class PayerFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = PayerSummary::class))
    val payers: List<PayerSummary>,
    @field:Schema(description = "총 페이지 수", example = "1")
    override val totalPage: Int
) : PageableResponse