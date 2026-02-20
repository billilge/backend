package site.billilge.api.backend.domain.item.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.exception.ItemErrorCode
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.external.FileStorageService

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository,
    private val fileStorageService: FileStorageService,
) {
    fun getAllItems(): List<Item> {
        return itemRepository.findAll()
    }

    fun getAllAdminItems(
        pageableCondition: PageableCondition,
        searchCondition: SearchCondition,
    ): Page<ItemWithRentCountQueryResult> {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.ASC, "name")
        )
        return itemRepository.findAllAsAdminItemDetailByKeyword(searchCondition.search, pageRequest)
    }

    @Transactional
    fun addItem(image: MultipartFile, name: String, type: ItemType, count: Int) {
        if (itemRepository.existsByName(name))
            throw ApiException(ItemErrorCode.ITEM_NAME_ALREADY_EXISTS)

        checkImageIsSvg(image)

        val imageUrl = fileStorageService.uploadImageFile(image)
            ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)

        val newItem = Item(
            name = name,
            type = type,
            count = count,
            imageUrl = imageUrl,
        )

        itemRepository.save(newItem)
    }

    @Transactional
    fun updateItem(image: MultipartFile?, itemId: Long, name: String, type: ItemType, count: Int) {
        val item = itemRepository.findById(itemId)
            .orElseThrow { ApiException(ItemErrorCode.ITEM_NOT_FOUND) }

        val imageUrl: String

        if (image == null || image.isEmpty) {
            imageUrl = item.imageUrl
        } else {
            checkImageIsSvg(image)
            imageUrl = fileStorageService.uploadImageFile(image)
                ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)
        }

        item.update(
            name = name,
            type = type,
            count = count,
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
            fileStorageService.deleteImageFile(imageUrl)
            return true
        }

        return false
    }

    fun checkImageIsSvg(image: MultipartFile) {
        if (image.contentType != "image/svg+xml")
            throw ApiException(ItemErrorCode.IMAGE_IS_NOT_SVG)
    }

    fun searchItems(searchCondition: SearchCondition): List<Item> {
        return itemRepository.findByItemName(searchCondition.search)
    }

    fun getItemById(itemId: Long): Item {
        return itemRepository.findById(itemId)
            .orElseThrow { ApiException(ItemErrorCode.ITEM_NOT_FOUND) }
    }
}
