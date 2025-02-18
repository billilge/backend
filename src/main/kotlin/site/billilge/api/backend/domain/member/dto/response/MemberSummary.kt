package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class MemberSummary(
    @field:Schema(description = "회원 이름", example = "황수민")
    val name: String,
    @field:Schema(description = "회원 학번", example = "20211234")
    val studentId: String
)
