package site.billilge.api.backend.domain.member.dto.request

data class AdminLoginRequest(
    val studentId: String,
    val password: String
)
