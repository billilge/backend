package site.billilge.api.backend.domain.admin.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class AdminErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    ITEM_NAME_ALREADY_EXISTS("이미 존재하는 물품 이름입니다.", HttpStatus.BAD_REQUEST),
    ITEM_NOT_FOUND("물품 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
}