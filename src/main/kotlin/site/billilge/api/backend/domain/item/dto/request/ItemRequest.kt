package site.billilge.api.backend.domain.item.dto.request

import site.billilge.api.backend.domain.item.enums.ItemType

data class ItemRequest(
    val name: String,
    val type: ItemType,
    val count: Int,
)
