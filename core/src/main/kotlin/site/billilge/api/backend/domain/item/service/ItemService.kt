package site.billilge.api.backend.domain.item.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.common.exception.GlobalErrorCode
import site.billilge.api.backend.core.port.FileStorageService
import site.billilge.api.backend.core.port.FileUploadRequest
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.item.appender.ItemAppender
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.exception.ItemErrorCode
import site.billilge.api.backend.domain.item.reader.ItemReader
import site.billilge.api.backend.domain.item.remover.ItemRemover
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemReader: ItemReader,
    private val itemAppender: ItemAppender,
    private val itemRemover: ItemRemover,
    private val fileStorageService: FileStorageService,
) {
    fun getAllItems(): List<Item> = itemReader.readAll()

    fun getAllAdminItems(keyword: String, pageRequest: PageRequest): PageResult<ItemWithRentCountQueryResult> =
        itemReader.readAllAsAdminDetail(keyword, pageRequest)

    fun searchItems(search: String): List<Item> = itemReader.readByItemName(search)

    fun getItemById(itemId: Long): Item = itemReader.read(itemId)

    @Transactional
    fun addItem(file: FileUploadRequest, name: String, type: ItemType, count: Int) {
        if (itemReader.existsByName(name)) throw ApiException(ItemErrorCode.ITEM_NAME_ALREADY_EXISTS)
        checkFileIsSvg(file)

        val imageUrl = fileStorageService.uploadImageFile(file)
            ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)

        try {
            itemAppender.save(Item(id = null, name = name, type = type, count = count, imageUrl = imageUrl))
        } catch (e: Exception) {
            runCatching { fileStorageService.deleteImageFile(imageUrl) }
                .onFailure { log.warn { "Failed to delete orphan image after save failure: $imageUrl" } }
            throw e
        }
    }

    @Transactional
    fun updateItem(file: FileUploadRequest?, itemId: Long, name: String, type: ItemType, count: Int) {
        val item = itemReader.read(itemId)
        val oldImageUrl = item.imageUrl

        val newImageUrl = if (file == null) {
            oldImageUrl
        } else {
            checkFileIsSvg(file)
            fileStorageService.uploadImageFile(file) ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)
        }

        try {
            itemAppender.save(item.copy(name = name, type = type, count = count, imageUrl = newImageUrl))
        } catch (e: Exception) {
            if (newImageUrl != oldImageUrl) {
                runCatching { fileStorageService.deleteImageFile(newImageUrl) }
                    .onFailure { log.warn { "Failed to delete orphan image after update failure: $newImageUrl" } }
            }
            throw e
        }

        if (newImageUrl != oldImageUrl) {
            runCatching { fileStorageService.deleteImageFile(oldImageUrl) }
                .onFailure { log.warn { "Failed to delete replaced image: $oldImageUrl" } }
        }
    }

    @Transactional
    fun deleteItem(itemId: Long) {
        val item = itemReader.read(itemId)
        itemRemover.remove(itemId)
        fileStorageService.deleteImageFile(item.imageUrl)
    }

    @Transactional
    fun adjustCount(itemId: Long, delta: Int) {
        val item = itemReader.read(itemId)
        itemAppender.save(item.copy(count = maxOf(0, item.count + delta)))
    }

    private fun checkFileIsSvg(file: FileUploadRequest) {
        if (file.contentType != "image/svg+xml") throw ApiException(ItemErrorCode.IMAGE_IS_NOT_SVG)
    }
}
