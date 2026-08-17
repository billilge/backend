package site.billilge.api.backend.domain.notification.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.notification.dto.PushDispatchTarget
import site.billilge.api.backend.global.external.fcm.FCMService
import site.billilge.api.backend.global.external.fcm.PushResult

private val log = KotlinLogging.logger {}

/**
 * 아웃박스에 등록된 푸시를 발송한다.
 *
 * 알림 저장 트랜잭션 바깥에서 호출된다. 네트워크 I/O가 DB 커넥션을 붙잡지 않도록
 * @Transactional을 걸지 않으며, 발송 실패를 예외로 전파하지 않는다.
 * 한 수신자의 실패가 다른 수신자에게 영향을 주면 안 되기 때문이다.
 *
 * 알림 발생 직후의 즉시 발송과 [PushRetryScheduler]의 재시도가 이 경로를 공유한다.
 */
@Component
class PushNotificationSender(
    private val fcmService: FCMService,
    private val memberService: MemberService,
    private val notificationPushOutboxService: NotificationPushOutboxService,
) {
    fun dispatch(outboxIds: List<Long>): List<PushResult> {
        return outboxIds.mapNotNull { outboxId -> dispatchOne(outboxId) }
    }

    /** 이미 처리됐거나 유효 시간이 지난 건은 건너뛰고 null을 반환한다 */
    private fun dispatchOne(outboxId: Long): PushResult? {
        val target = notificationPushOutboxService.findDispatchTarget(outboxId) ?: return null

        val result = send(target)

        notificationPushOutboxService.applyResult(outboxId, result)

        if (result == PushResult.InvalidToken) {
            memberService.clearFcmToken(target.receiverId)
        }

        return result
    }

    private fun send(target: PushDispatchTarget): PushResult {
        val fcmToken = target.fcmToken
            ?: return PushResult.Permanent(TOKEN_NOT_REGISTERED_REASON)

        return try {
            fcmService.sendPushNotification(
                fcmToken,
                target.status.title,
                target.status.formattedMessage(*target.formatValues.toTypedArray()),
                target.status.link,
                target.studentId
            )
        } catch (e: Exception) {
            // 메시지 포맷 인자 개수 불일치 등 발송 이전 단계에서 발생하는 오류
            log.error(e) { "(studentId=${target.studentId}) 푸시 메시지 생성 실패: ${e.message}" }
            PushResult.Permanent(e.javaClass.simpleName)
        }
    }

    companion object {
        private const val TOKEN_NOT_REGISTERED_REASON = "FCM_TOKEN_NOT_REGISTERED"
    }
}
