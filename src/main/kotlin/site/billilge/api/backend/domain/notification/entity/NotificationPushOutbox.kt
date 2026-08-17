package site.billilge.api.backend.domain.notification.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.enums.PushDeliveryStatus
import java.time.Duration
import java.time.LocalDateTime

/**
 * 푸시 발송 대기열.
 *
 * 알림 저장과 같은 트랜잭션에서 수신자 수만큼 생성된다. 발송 실패 시 상태와 재시도 시각만
 * 갱신되므로 프로세스가 재시작돼도 발송 대상이 남는다.
 *
 * 메시지 본문은 저장하지 않는다 — 연결된 [Notification]의 status와 formatValues로 재구성한다.
 */
@Entity
@Table(
    name = "notification_push_outbox",
    indexes = [
        Index(name = "idx_push_outbox_delivery", columnList = "delivery_status, next_retry_at"),
        Index(name = "idx_push_outbox_purge", columnList = "delivery_status, created_at"),
    ]
)
@EntityListeners(AuditingEntityListener::class)
class NotificationPushOutbox(
    @JoinColumn(name = "notification_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val notification: Notification,

    @JoinColumn(name = "receiver_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val receiver: Member,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_push_outbox_id", nullable = false)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    var deliveryStatus: PushDeliveryStatus = PushDeliveryStatus.PENDING
        protected set

    @Column(name = "retry_count", nullable = false)
    var retryCount: Int = 0
        protected set

    /**
     * 폴러는 이 시각이 지난 건만 집어간다.
     *
     * 생성 직후에는 이벤트 핸들러가 즉시 1회 발송을 시도하므로, 그 시도와 폴러가 겹쳐
     * 중복 발송되지 않도록 첫 대상 시각을 뒤로 미뤄 둔다.
     */
    @Column(name = "next_retry_at", nullable = false)
    var nextRetryAt: LocalDateTime = LocalDateTime.now().plus(FIRST_POLL_DELAY)
        protected set

    @Column(name = "last_error", length = MAX_ERROR_LENGTH)
    var lastError: String? = null
        protected set

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    fun markSent() {
        deliveryStatus = PushDeliveryStatus.SENT
        lastError = null
    }

    /** 재시도해도 결과가 같은 실패 — 더 시도하지 않는다 */
    fun markFailed(reason: String) {
        deliveryStatus = PushDeliveryStatus.FAILED
        lastError = reason.take(MAX_ERROR_LENGTH)
    }

    fun markExpired() {
        deliveryStatus = PushDeliveryStatus.EXPIRED
    }

    /**
     * 재시도 가능한 실패를 기록하고 다음 시도 시각을 뒤로 민다.
     * 재시도 횟수를 모두 썼거나 유효 시간이 지났으면 발송을 포기한다.
     */
    fun recordRetryableFailure(reason: String, now: LocalDateTime = LocalDateTime.now()) {
        lastError = reason.take(MAX_ERROR_LENGTH)

        if (isExpired(now)) {
            markExpired()
            return
        }

        if (retryCount >= BACKOFF_SECONDS.size) {
            deliveryStatus = PushDeliveryStatus.FAILED
            return
        }

        val nextAttemptAt = now.plusSeconds(BACKOFF_SECONDS[retryCount])

        // 다음 시도 시각이 이미 유효 시간을 넘긴다면 기다릴 이유가 없다
        if (isExpired(nextAttemptAt)) {
            markExpired()
            return
        }

        nextRetryAt = nextAttemptAt
        retryCount++
    }

    fun isPending(): Boolean = deliveryStatus == PushDeliveryStatus.PENDING

    /**
     * 유효 시간은 알림 종류가 정한다 — 대여 승인처럼 지금 행동을 결정하는 알림과
     * 관리자 대시보드에 남아 있는 작업 요청은 늦게 도착했을 때의 가치가 다르다.
     */
    fun isExpired(now: LocalDateTime = LocalDateTime.now()): Boolean =
        now.isAfter(createdAt.plus(notification.status.pushUrgency.timeToLive))

    companion object {
        /** 즉시 발송 시도와 폴러가 겹치지 않도록 두는 간격 */
        private val FIRST_POLL_DELAY: Duration = Duration.ofSeconds(60)

        /** 재시도 간격(초) — 배열 길이가 곧 최대 재시도 횟수 */
        private val BACKOFF_SECONDS = longArrayOf(30, 120, 300, 900)

        private const val MAX_ERROR_LENGTH = 500
    }
}
