package site.billilge.api.backend.domain.member.exception

import site.billilge.api.backend.common.exception.ErrorCode

enum class MemberErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", 400),
    EMAIL_ALREADY_EXISTS("이미 존재하는 회원의 이메일입니다.", 400),
    FORBIDDEN("접근 권한이 없습니다.", 403),
    ADMIN_PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다.", 400),
}
