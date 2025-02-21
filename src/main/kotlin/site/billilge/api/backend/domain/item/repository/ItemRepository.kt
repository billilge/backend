package site.billilge.api.backend.domain.item.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import site.billilge.api.backend.domain.item.entity.Item
import java.util.*

interface ItemRepository: JpaRepository<Item, Long>, ItemRepositoryCustom {
    override fun findById(id: Long): Optional<Item>
    fun existsByName(name: String): Boolean

    @Query("SELECT i FROM Item i WHERE i.name LIKE CONCAT('%', :search, '%')")
    fun findByItemName(@Param("search") search: String): List<Item>
}