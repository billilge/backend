package site.billilge.api.backend.domain.configvalue.exception

import site.billilge.api.backend.common.exception.ErrorCode

enum class ConfigValueErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    CONFIG_VALUE_NOT_FOUND("설정값을 찾을 수 없습니다.", 404),
    ADMIN_PASSWORD_MISMATCH("현재 비밀번호가 일치하지 않습니다.", 400),
}
