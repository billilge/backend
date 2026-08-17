package site.billilge.api.backend.domain.notification.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.global.external.fcm.FCMService
import site.billilge.api.backend.global.external.fcm.PushResult

private val log = KotlinLogging.logger {}

/**
 * FCM 푸시 발송 담당.
 *
 * 알림 저장 트랜잭션 바깥에서 호출된다. 네트워크 I/O가 DB 커넥션을 붙잡지 않도록
 * @Transactional을 걸지 않으며, 발송 실패를 예외로 전파하지 않는다.
 * 한 수신자의 실패가 다른 수신자나 이미 저장된 알림에 영향을 주면 안 되기 때문이다.
 */
@Component
class PushNotificationSender(
    private val fcmService: FCMService,
    private val memberService: MemberService,
) {
    fun send(member: Member, status: NotificationStatus, formatValues: List<String>) {
        val studentId = member.studentId
        val fcmToken = member.fcmToken

        if (fcmToken == null) {
            log.warn { "(studentId=$studentId) FCM Token is null" }
            return
        }

        val result = try {
            fcmService.sendPushNotification(
                fcmToken,
                status.title,
                status.formattedMessage(*formatValues.toTypedArray()),
                status.link,
                studentId
            )
        } catch (e: Exception) {
            // 메시지 포맷 인자 개수 불일치 등 발송 이전 단계에서 발생하는 오류
            log.error(e) { "(studentId=$studentId) 푸시 메시지 생성 실패: ${e.message}" }
            return
        }

        when (result) {
            PushResult.Success -> Unit
            PushResult.InvalidToken -> memberService.clearFcmToken(member.id!!)
            is PushResult.Retryable -> log.warn { "(studentId=$studentId) 재시도 가능한 푸시 실패: ${result.reason}" }
            is PushResult.Permanent -> log.error { "(studentId=$studentId) 재시도 불가능한 푸시 실패: ${result.reason}" }
        }
    }

    fun sendAll(members: List<Member>, status: NotificationStatus, formatValues: List<String>) {
        members.forEach { member ->
            send(member, status, formatValues)
        }
    }
}
