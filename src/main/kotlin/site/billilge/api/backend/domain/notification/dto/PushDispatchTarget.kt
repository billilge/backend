package site.billilge.api.backend.domain.notification.dto

import site.billilge.api.backend.domain.notification.enums.NotificationStatus

/**
 * 아웃박스 한 건을 발송하는 데 필요한 값.
 *
 * FCM 호출은 트랜잭션 밖에서 이뤄지므로 엔티티 대신 이 값만 꺼내 넘긴다.
 */
data class PushDispatchTarget(
    val outboxId: Long,
    val receiverId: Long,
    val studentId: String,
    val fcmToken: String?,
    val status: NotificationStatus,
    val formatValues: List<String>,
)
