package site.billilge.api.backend.domain.payer.dto.response

data class PayerFindAllResponse(
    val payers: List<PayerSummary>
)