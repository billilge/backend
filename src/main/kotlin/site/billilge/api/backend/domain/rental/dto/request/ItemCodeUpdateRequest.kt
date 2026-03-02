package site.billilge.api.backend.domain.rental.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ItemCodeUpdateRequest(
    @field:Schema(description = "물품 코드", example = "우산1")
    val itemCode: String
)
