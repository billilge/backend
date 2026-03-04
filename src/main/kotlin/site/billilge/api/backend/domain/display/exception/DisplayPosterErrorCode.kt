package site.billilge.api.backend.domain.display.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class DisplayPosterErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    POSTER_NOT_FOUND("포스터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
}
