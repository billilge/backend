package site.billilge.api.backend.domain.rental.enums

enum class RentalStatus(val displayName: String) {
    PENDING("승인 대기 중"),
    CANCEL("대기 취소"),
    CONFIRMED("소모품 대여 완료"),
    RENTAL("대여 중"),
    RETURNED("반납 완료")
}