package site.billilge.api.backend.domain.item.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.exception.ItemErrorCode
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult

@Component
class ItemReader(private val itemRepository: ItemRepository) {

    fun read(id: Long): Item =
        itemRepository.findById(id) ?: throw ApiException(ItemErrorCode.ITEM_NOT_FOUND)

    fun readAll(): List<Item> = itemRepository.findAll()

    fun readByItemName(search: String): List<Item> = itemRepository.findByItemName(search)

    fun readAllAsAdminDetail(keyword: String, pageRequest: PageRequest): PageResult<ItemWithRentCountQueryResult> =
        itemRepository.findAllAsAdminItemDetailByKeyword(keyword, pageRequest)

    fun existsByName(name: String): Boolean = itemRepository.existsByName(name)
}
