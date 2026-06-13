package site.billilge.api.backend.domain.item.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult

interface ItemRepository {
    fun findById(id: Long): Item?
    fun findAll(): List<Item>
    fun save(item: Item): Item
    fun delete(id: Long)
    fun existsByName(name: String): Boolean
    fun findByItemName(search: String): List<Item>
    fun findAllAsAdminItemDetailByKeyword(keyword: String, pageable: Pageable): Page<ItemWithRentCountQueryResult>
}
