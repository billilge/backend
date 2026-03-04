package site.billilge.api.backend.domain.display.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.display.entity.DisplayPoster
import java.time.LocalDateTime

@Schema
data class DisplayPosterFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = DisplayPosterDetail::class))
    val posters: List<DisplayPosterDetail>,
) {
    @Schema
    data class DisplayPosterDetail(
        @field:Schema(description = "포스터 ID", example = "1")
        val posterId: Long,
        @field:Schema(description = "포스터 제목", example = "학생회 행사 포스터")
        val title: String,
        @field:Schema(description = "포스터 이미지 URL", example = "https://example.com/poster.png")
        val imageUrl: String,
        @field:Schema(description = "활성화 여부", example = "true")
        val isActive: Boolean,
        @field:Schema(description = "생성일시", example = "2025-03-15T10:00:00")
        val createdAt: LocalDateTime,
    ) {
        companion object {
            @JvmStatic
            fun from(poster: DisplayPoster): DisplayPosterDetail {
                return DisplayPosterDetail(
                    posterId = poster.id!!,
                    title = poster.title,
                    imageUrl = poster.imageUrl,
                    isActive = poster.isActive,
                    createdAt = poster.createdAt,
                )
            }
        }
    }
}
