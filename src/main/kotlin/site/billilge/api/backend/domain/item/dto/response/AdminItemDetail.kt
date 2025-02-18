package site.billilge.api.backend.domain.item.dto.response

import site.billilge.api.backend.domain.item.enums.ItemType

data class AdminItemDetail(
    val itemId: Long,
    val itemName: String,
    val itemType: ItemType,
    val count: Int,
    val renterCount: Long,
    val imageUrl: String
)