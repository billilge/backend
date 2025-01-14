package site.billilge.api.backend.global.exception

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_AUTHORIZATION_HEADER("유효하지 않은 Authorization 헤더입니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    IMAGE_UPLOAD_FAILED("이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
}