package site.billilge.api.backend.domain.rental.enums

enum class RentalStatus(val displayName: String) {
    PENDING("승인 대기 중"),
    CANCEL("대기 취소"),
    CONFIRMED("승인 완료"),
    REJECTED("대여 반려"),
    RENTAL("대여 중"),
    RETURN_PENDING("반납 대기 중"),
    RETURN_CONFIRMED("반납 승인"),
    RETURNED("반납 완료")
}
