package site.billilge.api.backend.domain.configvalue.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class ConfigValueErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    CONFIG_VALUE_NOT_FOUND("설정값을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ADMIN_PASSWORD_MISMATCH("현재 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
}
