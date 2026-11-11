package site.billilge.api.backend.domain.notification.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.notification.service.NotificationPushOutboxService
import site.billilge.api.backend.domain.notification.service.PushNotificationSender
import site.billilge.api.backend.global.external.fcm.PushResult

private val log = KotlinLogging.logger {}

/**
 * 발송되지 못한 푸시를 주기적으로 재시도한다.
 *
 * fixedDelay라 이전 주기가 끝난 뒤에 다음 주기가 시작된다 — 발송이 밀려도 중복 실행되지 않는다.
 */
@Component
class PushRetryScheduler(
    private val notificationPushOutboxService: NotificationPushOutboxService,
    private val pushNotificationSender: PushNotificationSender,
) {
    @Scheduled(
        initialDelayString = "\${notification.push.retry.initial-delay-ms:60000}",
        fixedDelayString = "\${notification.push.retry.poll-interval-ms:30000}",
    )
    fun retryFailedPushes() {
        val targetIds = notificationPushOutboxService.findRetryTargetIds(BATCH_SIZE)

        if (targetIds.isEmpty()) return

        val results = pushNotificationSender.dispatch(targetIds)
        val sentCount = results.count { result -> result == PushResult.Success }

        log.info { "푸시 재시도 ${results.size}건 처리 (성공 $sentCount, 실패 ${results.size - sentCount})" }

        if (targetIds.size == BATCH_SIZE) {
            log.warn { "재시도 대상이 한 주기 처리량(${BATCH_SIZE}건)을 채웠습니다. 남은 건은 다음 주기에 처리됩니다." }
        }
    }

    companion object {
        private const val BATCH_SIZE = 100
    }
}
