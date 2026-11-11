package site.billilge.api.backend.domain.notification.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.notification.enums.PushDeliveryStatus
import site.billilge.api.backend.domain.notification.service.NotificationPushOutboxService
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

/**
 * 보존 기간이 지난 발송 대기열을 정리한다.
 *
 * 발송이 끝난 건은 폴러가 다시 조회하지 않지만, 알림 한 건마다 수신자 수만큼 쌓이므로
 * 방치하면 재시도 대상 조회가 느려진다.
 *
 * 재시도 폴러와 같은 스케줄러 스레드를 쓰므로, 한 회 처리량에 상한을 두어 폴러가 밀리지 않게 한다.
 */
@Component
class PushOutboxPurgeScheduler(
    private val notificationPushOutboxService: NotificationPushOutboxService,
) {
    @Scheduled(cron = "\${notification.push.outbox.purge-cron:0 0 4 * * *}", zone = "Asia/Seoul")
    fun purgeExpiredOutbox() {
        val now = LocalDateTime.now()

        val sentCount = purge(SENT_STATUSES, now.minusDays(SENT_RETENTION_DAYS))
        val givenUpCount = purge(GIVEN_UP_STATUSES, now.minusDays(GIVEN_UP_RETENTION_DAYS))

        if (sentCount == 0 && givenUpCount == 0) return

        log.info { "푸시 아웃박스 정리 완료 (발송 완료 ${sentCount}건, 발송 포기 ${givenUpCount}건 삭제)" }
    }

    private fun purge(deliveryStatuses: List<PushDeliveryStatus>, createdBefore: LocalDateTime): Int {
        var deletedCount = 0

        repeat(MAX_BATCHES_PER_RUN) {
            val deleted = notificationPushOutboxService.deletePurgeTargetBatch(
                deliveryStatuses,
                createdBefore,
                BATCH_SIZE
            )

            deletedCount += deleted

            if (deleted < BATCH_SIZE) return deletedCount
        }

        log.warn {
            "정리 대상이 한 회 처리량(${MAX_BATCHES_PER_RUN * BATCH_SIZE}건)을 채웠습니다. " +
                "남은 건은 다음 주기에 정리됩니다. statuses=$deliveryStatuses"
        }

        return deletedCount
    }

    companion object {
        private val SENT_STATUSES = listOf(PushDeliveryStatus.SENT)

        /** 실패 원인(last_error)을 들여다볼 여지를 남기기 위해 더 오래 보관한다 */
        private val GIVEN_UP_STATUSES = listOf(PushDeliveryStatus.FAILED, PushDeliveryStatus.EXPIRED)

        private const val SENT_RETENTION_DAYS = 7L
        private const val GIVEN_UP_RETENTION_DAYS = 30L

        private const val BATCH_SIZE = 500
        private const val MAX_BATCHES_PER_RUN = 20
    }
}
