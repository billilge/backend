package site.billilge.api.backend.domain.rental.event

import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

data class RentalAppliedEvent(
    val memberId: Long,
    val itemName: String,
    val rentAt: LocalDateTime,
    val isDevMode: Boolean,
)

data class RentalCancelledEvent(
    val memberId: Long,
    val itemName: String,
)

data class ReturnAppliedEvent(
    val memberId: Long,
    val itemName: String,
)

data class RentalStatusChangedEvent(
    val memberId: Long,
    val itemName: String,
    val status: RentalStatus,
)
