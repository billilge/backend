package site.billilge.api.backend.domain.payer.dto.response

import site.billilge.api.backend.domain.payer.entity.Payer

data class PayerSummary(
    val payerId: Long,
    val name: String,
    val studentId: String,
    val registered: Boolean
) {
    companion object {
        @JvmStatic
        fun from(payer: Payer): PayerSummary {
            val studentId = payer.studentId ?: "${payer.enrollmentYear}XXXX"

            return PayerSummary(
                payerId = payer.id!!,
                name = payer.name,
                studentId = studentId,
                registered = payer.registered
            )
        }
    }
}
