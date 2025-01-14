package site.billilge.api.backend.domain.item.dto.response

import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.exception.ItemErrorCode
import site.billilge.api.backend.global.exception.ApiException

data class ItemDetail(
    val itemId: Long,
    val itemName: String,
    val itemType: ItemType,
    val count: Int,
    val imageUrl: String,
) {
    companion object {
        @JvmStatic
        fun from(item: Item): ItemDetail {
            return ItemDetail(
                itemId = item.id
                    ?: throw ApiException(ItemErrorCode.ITEM_ID_IS_NULL),
                itemName = item.name,
                itemType = item.type,
                count = item.count,
                imageUrl = item.imageUrl
            )
        }
    }
}
