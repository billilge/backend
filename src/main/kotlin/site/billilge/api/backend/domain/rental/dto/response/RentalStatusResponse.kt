package site.billilge.api.backend.domain.rental.dto.response

data class RentalStatusResponse(
    val itemId: Long,
    val isRented: Boolean,
)
