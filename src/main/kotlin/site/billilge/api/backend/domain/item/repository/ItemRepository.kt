package site.billilge.api.backend.domain.item.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.item.entity.Item
import java.util.*

interface ItemRepository: JpaRepository<Item, Long> {
    override fun findById(id: Long): Optional<Item>
    fun existsByName(name: String): Boolean
}