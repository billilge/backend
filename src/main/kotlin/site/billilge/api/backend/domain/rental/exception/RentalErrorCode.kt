package site.billilge.api.backend.domain.rental.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class RentalErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    ITEM_NOT_FOUND("물품 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ITEM_OUT_OF_STOCK("물품의 재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    RENTAL_ITEM_DUPLICATED("이미 대여 중인 물품입니다.\n중복 대여하시겠습니까?", HttpStatus.CONFLICT),
    INVALID_RENTAL_TIME_OUT_OF_RANGE("대여 가능시간은 10:00 ~ 17:00입니다. 다시 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_RENTAL_TIME_PAST("현재 시간보다 이후의 시간으로 설정해주세요", HttpStatus.BAD_REQUEST)
}