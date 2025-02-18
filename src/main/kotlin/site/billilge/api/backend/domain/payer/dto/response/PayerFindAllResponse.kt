package site.billilge.api.backend.domain.payer.dto.response

import site.billilge.api.backend.global.dto.PageableResponse

data class PayerFindAllResponse(
    val payers: List<PayerSummary>,
    override val totalPage: Int
) : PageableResponse