package site.billilge.api.backend.domain.notification.enums

import java.time.Duration

/**
 * 푸시가 얼마나 늦게까지 유효한지.
 *
 * 유효 시간이 지나면 발송을 포기한다. 인앱 알림은 이미 저장돼 있으므로 정보가 사라지는 것은 아니고,
 * "지금 알림으로 끼어들 가치가 있는가"의 기준이다.
 */
enum class PushUrgency(val timeToLive: Duration) {
    /**
     * 사용자가 지금 무엇을 할지 결정하는 정보 — 늦게 도착하면 오히려 혼란스럽다.
     * 대여 승인 알림을 받고 과방에 가려는데 알림이 한참 뒤에 오면 소용이 없다.
     */
    IMMEDIATE(Duration.ofMinutes(15)),

    /**
     * 관리자 대시보드에 처리할 건이 그대로 남아 있어 늦게 도착해도 유효하다.
     */
    DEFERRABLE(Duration.ofHours(1)),
}
