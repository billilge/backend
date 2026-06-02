package site.billilge.api.backend.domain.item.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult
import site.billilge.api.backend.domain.item.repository.dto.StockInconsistencyQueryResult

interface ItemRepositoryCustom {
    fun findAllAsAdminItemDetailByKeyword(keyword: String, pageable: Pageable): Page<ItemWithRentCountQueryResult>
    fun findStockInconsistencies(): List<StockInconsistencyQueryResult>
}
