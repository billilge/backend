package site.billilge.api.backend.domain.item.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.item.entity.Item

@Schema
data class ItemSummary(
    @field:Schema(description = "물품 이름", example = "우산")
    val itemName: String,
    @field:Schema(description = "물품 아이콘 URL", example = "https://www.example.com/test.svg")
    val imageUrl: String,
) {
    companion object {
        @JvmStatic
        fun from(item: Item): ItemSummary {
            return ItemSummary(
                itemName = item.name,
                imageUrl = item.imageUrl
            )
        }
    }
}
