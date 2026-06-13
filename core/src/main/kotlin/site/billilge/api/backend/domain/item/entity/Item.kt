package site.billilge.api.backend.domain.item.entity

import site.billilge.api.backend.domain.item.enums.ItemType

data class Item(
    val id: Long?,
    val name: String,
    val type: ItemType,
    val count: Int,
    val imageUrl: String,
)
