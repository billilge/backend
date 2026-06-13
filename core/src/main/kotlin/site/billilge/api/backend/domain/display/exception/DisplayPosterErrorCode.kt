package site.billilge.api.backend.domain.display.exception

import site.billilge.api.backend.common.exception.ErrorCode

enum class DisplayPosterErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    POSTER_NOT_FOUND("포스터를 찾을 수 없습니다.", 404),
}
