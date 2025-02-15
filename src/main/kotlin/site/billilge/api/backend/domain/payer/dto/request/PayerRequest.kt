package site.billilge.api.backend.domain.payer.dto.request

data class PayerRequest(
    val payers: List<PayerItem>
) {
    data class PayerItem(
        val name: String,
        val studentId: String
    )
}
