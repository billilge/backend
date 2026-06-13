package site.billilge.api.backend.domain.payer.entity

data class Payer(
    val id: Long?,
    val name: String,
    val enrollmentYear: String,
    val studentId: String? = null,
    val registered: Boolean = false,
)
