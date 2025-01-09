package site.billilge.api.backend.domain.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.admin.exception.AdminErrorCode
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.global.exception.ApiException

@Service
@Transactional(readOnly = true)
class AdminService(
    private val itemRepository: ItemRepository
) {
    fun getAllItems(): ItemFindAllResponse {
        val itemDetails = itemRepository.findAll()
            .map { item -> ItemDetail.from(item) }

        return ItemFindAllResponse(itemDetails)
    }

    //TODO: 아이템 이미지 S3에 업로드하도록 구현하기
    @Transactional
    fun addItem(image: MultipartFile, itemRequest: ItemRequest) {
        if (itemRepository.existsByName(itemRequest.name))
            throw ApiException(AdminErrorCode.ITEM_NAME_ALREADY_EXISTS)

        val imageUrl = "dummy"
        val type = if (itemRequest.isConsumption) ItemType.CONSUMPTION else ItemType.RENTAL

        val newItem = Item(
            name = itemRequest.name,
            type = type,
            count = itemRequest.count,
            imageUrl = imageUrl,
        )

        itemRepository.save(newItem)
    }

    @Transactional
    fun updateItem(image: MultipartFile?, itemId: Long, itemRequest: ItemRequest) {
        val item = itemRepository.findById(itemId)
            .orElseThrow { ApiException(AdminErrorCode.ITEM_NOT_FOUND) }
        val imageUrl = image.toString()
        val type = if (itemRequest.isConsumption) ItemType.CONSUMPTION else ItemType.RENTAL

        item.update(
            name = itemRequest.name,
            type = type,
            count = itemRequest.count,
            imageUrl = imageUrl,
        )
    }
}