package site.billilge.api.backend.domain.item.repository.dto

data class StockInconsistencyQueryResult(
    val itemId: Long,
    val itemName: String,
    val currentCount: Int,
    val totalCount: Int,
    val activeRentedSum: Int,
)
