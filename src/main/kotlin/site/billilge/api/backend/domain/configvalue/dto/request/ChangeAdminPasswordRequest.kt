package site.billilge.api.backend.domain.configvalue.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ChangeAdminPasswordRequest(
    @field:Schema(description = "현재 비밀번호", example = "0412")
    val currentPassword: String,
    @field:Schema(description = "새 비밀번호", example = "1234")
    val newPassword: String,
)
