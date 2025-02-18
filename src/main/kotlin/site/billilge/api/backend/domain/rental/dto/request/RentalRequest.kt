package site.billilge.api.backend.domain.rental.dto.request

data class RentalTime(
    val hour: Int,
    val minute: Int
)

data class RentalRequest(
    val itemId: Long,
    val count: Int,
    val rentalTime: RentalTime,
    val ignoreDuplicate: Boolean
)
