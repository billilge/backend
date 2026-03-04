package site.billilge.api.backend.domain.display.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class DisplayPosterRequest(
    @field:Schema(description = "포스터 제목", example = "학생회 행사 포스터")
    val title: String,
)
