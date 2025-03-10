package site.billilge.api.backend.domain.item.repository

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import site.billilge.api.backend.domain.item.dto.response.AdminItemDetail
import site.billilge.api.backend.domain.item.entity.QItem
import site.billilge.api.backend.domain.rental.entity.QRentalHistory

class ItemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ItemRepositoryCustom {
    override fun findAllAsAdminItemDetailByKeyword(keyword: String, pageable: Pageable): Page<AdminItemDetail> {
        val item = QItem.item
        val rentalHistory = QRentalHistory.rentalHistory

        val contents = queryFactory
            .select(
                Projections.constructor(
                    AdminItemDetail::class.java,
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
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(item.count())
            .from(item)
            .where(searchCondition(item, keyword))

        return PageableExecutionUtils.getPage(contents, pageable) { count.fetchOne() ?: 0 }
    }

    private fun searchCondition(item: QItem, keyword: String): BooleanExpression {
        return item.name.like("%${keyword}%")
    }
}