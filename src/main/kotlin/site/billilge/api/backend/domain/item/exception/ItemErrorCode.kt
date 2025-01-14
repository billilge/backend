package site.billilge.api.backend.domain.item.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class ItemErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    ITEM_ID_IS_NULL("물품의 ID를 가져올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
}