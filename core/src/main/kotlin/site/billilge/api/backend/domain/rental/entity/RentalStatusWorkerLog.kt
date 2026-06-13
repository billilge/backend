package site.billilge.api.backend.domain.rental.entity

import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

data class RentalStatusWorkerLog(
    val id: Long?,
    val rentalHistoryId: Long,
    val rentalStatus: RentalStatus,
    val worker: Member? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
