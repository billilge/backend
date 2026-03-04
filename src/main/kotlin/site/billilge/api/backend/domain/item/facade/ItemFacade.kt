package site.billilge.api.backend.domain.item.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.global.dto.SearchCondition

@Component
class ItemFacade(
    private val itemService: ItemService,
) {
    fun searchItems(searchCondition: SearchCondition): ItemFindAllResponse {
        val items = itemService.searchItems(searchCondition)
        return ItemFindAllResponse(items.map { ItemDetail.from(it) })
    }
}
