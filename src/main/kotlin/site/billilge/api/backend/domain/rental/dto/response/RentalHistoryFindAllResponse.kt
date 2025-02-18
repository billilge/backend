package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

data class RentalHistoryFindAllResponse(
    @ArraySchema(schema = Schema(implementation = RentalHistoryDetail::class))
    val rentalHistories: List<RentalHistoryDetail>
)
