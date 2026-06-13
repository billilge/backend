package site.billilge.api.backend.domain.item.repository

import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult

interface ItemRepository {
    fun findById(id: Long): Item?
    fun findAll(): List<Item>
    fun save(item: Item): Item
    fun delete(id: Long)
    fun existsByName(name: String): Boolean
    fun findByItemName(search: String): List<Item>
    fun findAllAsAdminItemDetailByKeyword(keyword: String, pageRequest: PageRequest): PageResult<ItemWithRentCountQueryResult>
}
