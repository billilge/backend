package site.billilge.api.backend.domain.member.dto.request

data class SignUpRequest(
    val email: String,
    val studentId: String,
    val name: String
)
