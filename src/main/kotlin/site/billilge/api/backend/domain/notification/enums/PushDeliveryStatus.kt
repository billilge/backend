package site.billilge.api.backend.domain.notification.enums

enum class PushDeliveryStatus {
    /** 발송 대기 — 폴러가 재시도 대상으로 집어간다 */
    PENDING,

    /** 발송 완료 */
    SENT,

    /** 재시도 횟수를 모두 썼거나 재시도해도 소용없는 실패 */
    FAILED,

    /** 유효 시간이 지나 발송을 포기함 — 늦게 도착하는 푸시는 의미가 없다 */
    EXPIRED,
}
