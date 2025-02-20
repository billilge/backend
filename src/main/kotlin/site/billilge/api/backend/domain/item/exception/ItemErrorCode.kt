package site.billilge.api.backend.domain.item.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class ItemErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    ITEM_ID_IS_NULL("물품의 ID를 가져올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ITEM_NAME_ALREADY_EXISTS("이미 존재하는 물품 이름입니다.", HttpStatus.BAD_REQUEST),
    ITEM_NOT_FOUND("물품 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    IMAGE_IS_NOT_SVG("이미지 파일은 svg 확장자만 가능합니다.", HttpStatus.BAD_REQUEST),
}