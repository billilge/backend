package site.billilge.api.backend.domain.item.facade

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.AdminItemDetail
import site.billilge.api.backend.domain.item.dto.response.AdminItemFindAllResponse
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@Component
class AdminItemFacade(
    private val itemService: ItemService,
) {
    fun getAllAdminItems(pageableCondition: PageableCondition, searchCondition: SearchCondition): AdminItemFindAllResponse {
        val page = itemService.getAllAdminItems(pageableCondition, searchCondition)
        val adminItemDetails = page.content.map {
            AdminItemDetail(
                itemId = it.itemId,
                itemName = it.itemName,
                itemType = it.itemType,
                count = it.count,
                renterCount = it.renterCount,
                imageUrl = it.imageUrl,
            )
        }
        return AdminItemFindAllResponse(adminItemDetails, page.totalPages)
    }

    fun addItem(image: MultipartFile, itemRequest: ItemRequest) {
        itemService.addItem(image, itemRequest.name, itemRequest.type, itemRequest.count)
    }

    fun updateItem(image: MultipartFile?, itemId: Long, itemRequest: ItemRequest) {
        itemService.updateItem(image, itemId, itemRequest.name, itemRequest.type, itemRequest.count)
    }

    fun getItemById(itemId: Long): ItemDetail {
        val item = itemService.getItemById(itemId)
        return ItemDetail.from(item)
    }

    fun deleteItem(itemId: Long): Boolean {
        return itemService.deleteItem(itemId)
    }
}
