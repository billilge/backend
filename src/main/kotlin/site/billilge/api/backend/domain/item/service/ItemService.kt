package site.billilge.api.backend.domain.item.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.exception.ItemErrorCode
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.external.s3.S3Service

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository,
    private val s3Service: S3Service,
) {
    fun getAllItems(): ItemFindAllResponse {
        val itemDetails = itemRepository.findAll()
            .map { item -> ItemDetail.from(item) }

        return ItemFindAllResponse(itemDetails)
    }

    @Transactional
    fun addItem(image: MultipartFile, itemRequest: ItemRequest) {
        if (itemRepository.existsByName(itemRequest.name))
            throw ApiException(ItemErrorCode.ITEM_NAME_ALREADY_EXISTS)

        val imageUrl = s3Service.uploadImageFile(image)
            ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)

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
            .orElseThrow { ApiException(ItemErrorCode.ITEM_NOT_FOUND) }
        val imageUrl = image?.let { s3Service.uploadImageFile(it) }
            ?: item.imageUrl
        val type = if (itemRequest.isConsumption) ItemType.CONSUMPTION else ItemType.RENTAL

        item.update(
            name = itemRequest.name,
            type = type,
            count = itemRequest.count,
            imageUrl = imageUrl,
        )
    }

    @Transactional
    fun deleteItem(itemId: Long): Boolean {
        val item = itemRepository.findById(itemId)
            .orElseThrow { ApiException(ItemErrorCode.ITEM_NOT_FOUND) }

        val imageUrl = item.imageUrl
        var isEntityDeleted: Boolean

        try {
            itemRepository.deleteById(itemId)
            isEntityDeleted = true
        } catch (e: Exception) {
            e.printStackTrace()
            isEntityDeleted = false
        }

        if (isEntityDeleted) {
            s3Service.deleteImageFile(imageUrl)
            return true
        }

        return false
    }
}