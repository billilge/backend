package site.billilge.api.backend.domain.notification.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.dto.PushDispatchTarget
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.entity.NotificationPushOutbox
import site.billilge.api.backend.domain.notification.enums.PushDeliveryStatus
import site.billilge.api.backend.domain.notification.repository.NotificationPushOutboxRepository
import site.billilge.api.backend.global.external.fcm.PushResult
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class NotificationPushOutboxService(
    private val notificationPushOutboxRepository: NotificationPushOutboxRepository,
) {
    /**
     * 발송 대기열에 수신자 수만큼 등록한다.
     *
     * 알림 저장 트랜잭션에 참여하므로, 알림과 발송 대상은 함께 저장되거나 함께 롤백된다.
     */
    @Transactional
    fun register(notification: Notification, receivers: List<Member>): List<Long> {
        return receivers
            .filter { receiver -> receiver.hasFcmToken() }
            .map { receiver ->
                notificationPushOutboxRepository.save(NotificationPushOutbox(notification, receiver)).id!!
            }
    }

    fun findRetryTargetIds(limit: Int): List<Long> {
        return notificationPushOutboxRepository.findDispatchTargetIds(
            PushDeliveryStatus.PENDING,
            LocalDateTime.now(),
            PageRequest.of(0, limit)
        )
    }

    /**
     * 발송에 필요한 값을 꺼내 온다. 이미 처리됐거나 유효 시간이 지난 건은 null을 반환한다.
     */
    @Transactional
    fun findDispatchTarget(outboxId: Long): PushDispatchTarget? {
        val outbox = notificationPushOutboxRepository.findByIdOrNull(outboxId) ?: return null

        if (!outbox.isPending()) return null

        if (outbox.isExpired()) {
            outbox.markExpired()
            log.warn { "(outboxId=$outboxId) 유효 시간이 지나 푸시 발송을 포기합니다." }
            return null
        }

        val receiver = outbox.receiver
        val notification = outbox.notification

        return PushDispatchTarget(
            outboxId = outboxId,
            receiverId = receiver.id!!,
            studentId = receiver.studentId,
            fcmToken = receiver.fcmToken,
            status = notification.status,
            formatValues = notification.formatValueList,
        )
    }

    @Transactional
    fun applyResult(outboxId: Long, result: PushResult) {
        val outbox = notificationPushOutboxRepository.findByIdOrNull(outboxId) ?: return

        when (result) {
            PushResult.Success -> outbox.markSent()
            PushResult.InvalidToken -> outbox.markFailed(INVALID_TOKEN_REASON)
            is PushResult.Permanent -> outbox.markFailed(result.reason)
            is PushResult.Retryable -> outbox.recordRetryableFailure(result.reason)
        }

        if (outbox.deliveryStatus == PushDeliveryStatus.FAILED || outbox.deliveryStatus == PushDeliveryStatus.EXPIRED) {
            log.error {
                "(outboxId=$outboxId) 푸시 발송을 포기합니다. " +
                    "status=${outbox.deliveryStatus}, retryCount=${outbox.retryCount}, reason=${outbox.lastError}"
            }
        }
    }

    private fun Member.hasFcmToken(): Boolean {
        if (fcmToken != null) return true

        log.warn { "(studentId=$studentId) FCM Token is null" }
        return false
    }

    companion object {
        private const val INVALID_TOKEN_REASON = "INVALID_TOKEN"
    }
}
