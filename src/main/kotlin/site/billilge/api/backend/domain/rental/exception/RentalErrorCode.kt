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
    INVALID_RENTAL_TIME_PAST("현재 시간보다 이후의 시간으로 설정해주세요", HttpStatus.BAD_REQUEST),
    INVALID_RENTAL_TIME_WEEKEND("주말에는 대여가 불가능합니다.", HttpStatus.BAD_REQUEST),
    RENTAL_NOT_FOUND("대여 기록을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    MEMBER_IS_NOT_PAYER("복지물품을 대여하려면 먼저 학생회비를 납부해주세요.", HttpStatus.FORBIDDEN),
    TODAY_IS_IN_EXAM_PERIOD("시험기간(04.14.~04.28.)에는\n대여가 불가능합니다.\n양해 부탁드립니다.", HttpStatus.BAD_REQUEST),
}