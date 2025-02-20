package site.billilge.api.backend.domain.item.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.item.enums.ItemType

@Schema
data class AdminItemDetail(
    @field:Schema(description = "물품 ID", example = "12")
    val itemId: Long,
    @field:Schema(description = "물품 이름", example = "우산")
    val itemName: String,
    @field:Schema(description = "물품 타입", example = "RENTAL")
    val itemType: ItemType,
    @field:Schema(description = "물품 개수", example = "10")
    val count: Int,
    @field:Schema(description = "대여 기록 수", example = "20")
    val renterCount: Long,
    @field:Schema(description = "물품 아이콘 URL", example = "https://www.example.com/test.svg")
    val imageUrl: String
)