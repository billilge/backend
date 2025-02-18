package site.billilge.api.backend.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class SignUpRequest(
    @field:Schema(description = "이메일", example = "test@kookmin.ac.kr")
    val email: String,
    @field:Schema(description = "학번", example = "20211234")
    val studentId: String,
    @field:Schema(description = "이름", example = "황수민")
    val name: String
)
