package site.billilge.api.backend.global.exception

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_AUTHORIZATION_HEADER("유효하지 않은 Authorization 헤더입니다.", HttpStatus.BAD_REQUEST),
}