package site.billilge.api.backend.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class AdminRequest(
    @field:Schema(description = "회원 ID 목록", example = "[1, 2, 3]")
    val memberIds: List<Long>
)
