package site.billilge.api.backend.domain.configvalue.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ConfigValueUpdateRequest(
    @field:Schema(description = "설정 키", example = "exam-period.start-date")
    val key: String,
    @field:Schema(description = "설정 값", example = "2025-04-14")
    val value: String,
)
