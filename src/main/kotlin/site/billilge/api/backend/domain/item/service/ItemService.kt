package site.billilge.api.backend.domain.item.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.AdminItemFindAllResponse
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.exception.ItemErrorCode
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
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
            .map { ItemDetail.from(it) }

        return ItemFindAllResponse(itemDetails)
    }

    fun getAllAdminItems(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition,
    ): AdminItemFindAllResponse {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.ASC, "name")
        )
        val adminItemDetails = itemRepository.findAllAsAdminItemDetailByKeyword(searchCondition.search, pageRequest)

        return AdminItemFindAllResponse(adminItemDetails.content, adminItemDetails.totalPages)
    }

    @Transactional
    fun addItem(image: MultipartFile, itemRequest: ItemRequest) {
        if (itemRepository.existsByName(itemRequest.name))
            throw ApiException(ItemErrorCode.ITEM_NAME_ALREADY_EXISTS)

        checkImageIsSvg(image)

        val imageUrl = s3Service.uploadImageFile(image)
            ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)

        val newItem = Item(
            name = itemRequest.name,
            type = itemRequest.type,
            count = itemRequest.count,
            imageUrl = imageUrl,
        )

        itemRepository.save(newItem)
    }

    @Transactional
    fun updateItem(image: MultipartFile?, itemId: Long, itemRequest: ItemRequest) {
        val item = itemRepository.findById(itemId)
            .orElseThrow { ApiException(ItemErrorCode.ITEM_NOT_FOUND) }

        val imageUrl: String

        if (image == null || image.isEmpty) {
            imageUrl = item.imageUrl
        } else {
            checkImageIsSvg(image)
            imageUrl = s3Service.uploadImageFile(image)
                ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)
        }

        item.update(
            name = itemRequest.name,
            type = itemRequest.type,
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

    fun checkImageIsSvg(image: MultipartFile) {
        if (image.contentType != "image/svg+xml")
            throw ApiException(ItemErrorCode.IMAGE_IS_NOT_SVG)
    }
}