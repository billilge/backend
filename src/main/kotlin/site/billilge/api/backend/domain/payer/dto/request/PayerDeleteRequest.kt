package site.billilge.api.backend.domain.payer.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class PayerDeleteRequest(
    @field:Schema(description = "납부자 ID 목록", example = "[1, 2, 3]")
    val payerIds: List<Long>
)
