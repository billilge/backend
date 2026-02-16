package site.billilge.api.backend.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.enums.Role

@Schema
data class AdminRequest(
    @field:Schema(description = "회원 ID 목록", example = "[1, 2, 3]")
    val memberIds: List<Long>,
    @field:Schema(description = "관리자 역할", example = "ADMIN")
    val role: Role = Role.ADMIN,
)
