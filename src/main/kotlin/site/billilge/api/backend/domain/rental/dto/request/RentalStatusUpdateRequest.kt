package site.billilge.api.backend.domain.rental.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.rental.enums.RentalStatus

@Schema
data class RentalStatusUpdateRequest(
    @field:Schema(description = "변경할 대여 상태", example = "CONFIRMED")
    val rentalStatus: RentalStatus
)
