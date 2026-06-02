package site.billilge.api.backend.domain.item.repository

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import site.billilge.api.backend.domain.item.entity.QItem
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.repository.dto.ItemWithRentCountQueryResult
import site.billilge.api.backend.domain.item.repository.dto.StockInconsistencyQueryResult
import site.billilge.api.backend.domain.rental.entity.QRentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus

class ItemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ItemRepositoryCustom {
    override fun findAllAsAdminItemDetailByKeyword(keyword: String, pageable: Pageable): Page<ItemWithRentCountQueryResult> {
        val item = QItem.item
        val rentalHistory = QRentalHistory.rentalHistory

        val contents = queryFactory
            .select(
                Projections.constructor(
                    ItemWithRentCountQueryResult::class.java,
                    item.id.`as`("itemId"),
                    item.name.`as`("itemName"),
                    item.type.`as`("itemType"),
                    item.count,
                    ExpressionUtils.`as`(
                        JPAExpressions
                            .select(rentalHistory.count())
                            .from(rentalHistory)
                            .where(rentalHistory.item.eq(item)),
                        "renterCount"
                    ),
                    item.imageUrl
                )
            )
            .from(item)
            .where(searchCondition(item, keyword))
            .orderBy(item.name.asc(), item.id.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(item.count())
            .from(item)
            .where(searchCondition(item, keyword))

        return PageableExecutionUtils.getPage(contents, pageable) { count.fetchOne() ?: 0 }
    }

    override fun findStockInconsistencies(): List<StockInconsistencyQueryResult> {
        val item = QItem.item
        val rental = QRentalHistory.rentalHistory

        val activeRentedSum = rental.rentedCount.sum().coalesce(0)

        return queryFactory
            .select(
                Projections.constructor(
                    StockInconsistencyQueryResult::class.java,
                    item.id,
                    item.name,
                    item.count,
                    item.totalCount,
                    activeRentedSum,
                )
            )
            .from(item)
            .leftJoin(rental).on(
                rental.item.eq(item)
                    .and(rental.rentalStatus.`in`(STOCK_CONSUMING_STATUSES))
            )
            .where(item.type.eq(ItemType.RENTAL))
            .groupBy(item.id, item.name, item.count, item.totalCount)
            .having(item.count.ne(item.totalCount.subtract(activeRentedSum)))
            .fetch()
    }

    private fun searchCondition(item: QItem, keyword: String): BooleanExpression {
        return item.name.like("%${keyword}%")
    }

    companion object {
        private val STOCK_CONSUMING_STATUSES = listOf(
            RentalStatus.CONFIRMED,
            RentalStatus.RENTAL,
            RentalStatus.RETURN_PENDING,
            RentalStatus.RETURN_CONFIRMED,
        )
    }
}
