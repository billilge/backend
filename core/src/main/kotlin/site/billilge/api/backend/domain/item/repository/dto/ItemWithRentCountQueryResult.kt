package site.billilge.api.backend.domain.item.repository.dto

import site.billilge.api.backend.domain.item.enums.ItemType

data class ItemWithRentCountQueryResult(
    val itemId: Long,
    val itemName: String,
    val itemType: ItemType,
    val count: Int,
    val renterCount: Long,
    val imageUrl: String,
)
