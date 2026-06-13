package site.billilge.api.backend.domain.item.exception

import site.billilge.api.backend.common.exception.ErrorCode

enum class ItemErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    ITEM_ID_IS_NULL("물품의 ID를 가져올 수 없습니다.", 500),
    ITEM_NAME_ALREADY_EXISTS("이미 존재하는 물품 이름입니다.", 400),
    ITEM_NOT_FOUND("물품 정보를 찾을 수 없습니다.", 404),
    IMAGE_IS_NOT_SVG("이미지 파일은 svg 확장자만 가능합니다.", 400),
}
