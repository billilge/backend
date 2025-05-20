package site.billilge.api.backend.domain.payer.event

sealed class PayerEvent(open val payerIds: List<Long?>)

data class PayerAddEvent(override val payerIds: List<Long?>) : PayerEvent(payerIds)

data class PayerDeleteEvent(override val payerIds: List<Long?>) : PayerEvent(payerIds)