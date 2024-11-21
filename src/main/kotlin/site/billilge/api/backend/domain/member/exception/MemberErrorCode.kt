package site.billilge.api.backend.domain.member.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class MemberErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
}