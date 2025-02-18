package site.billilge.api.backend.domain.item.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.item.enums.ItemType

@Schema
data class ItemRequest(
    @field:Schema(description = "물품 이름", example = "우산")
    val name: String,
    @field:Schema(description = "물품 타입", example = "RENTAL")
    val type: ItemType,
    @field:Schema(description = "물품 개수", example = "10")
    val count: Int,
)
