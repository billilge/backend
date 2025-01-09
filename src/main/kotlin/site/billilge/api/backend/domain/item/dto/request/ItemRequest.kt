package site.billilge.api.backend.domain.item.dto.request

data class ItemRequest(
    val name: String,
    val isConsumption: Boolean,
    val count: Int,
)
