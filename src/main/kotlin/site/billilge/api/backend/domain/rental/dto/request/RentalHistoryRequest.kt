package site.billilge.api.backend.domain.rental.dto.request

data class RentalHistoryRequest(
    val itemId: Long,
    val count: Int,
    val rentalTime: RentalTime,
    val ignoreDuplicate: Boolean
) {
    data class RentalTime(
        val hour: Int,
        val minute: Int
    )
}