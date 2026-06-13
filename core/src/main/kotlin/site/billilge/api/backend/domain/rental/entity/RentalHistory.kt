package site.billilge.api.backend.domain.rental.entity

import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

data class RentalHistory(
    val id: Long?,
    val member: Member,
    val item: Item,
    val rentalStatus: RentalStatus,
    val rentAt: LocalDateTime,
    val returnedAt: LocalDateTime? = null,
    val rentedCount: Int,
    val itemCode: String? = null,
    val worker: Member? = null,
    val applicatedAt: LocalDateTime = LocalDateTime.now(),
)
