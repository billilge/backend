package site.billilge.api.backend.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.enums.Role

@Schema
data class AdminRoleUpdateRequest(
    @field:Schema(description = "변경할 역할", example = "GA")
    val role: Role,
)
