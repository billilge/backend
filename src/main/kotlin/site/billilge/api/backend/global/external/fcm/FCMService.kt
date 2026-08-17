package site.billilge.api.backend.global.external.fcm

import com.google.firebase.messaging.*
import org.springframework.stereotype.Service
import site.billilge.api.backend.global.logging.log

@Service
class FCMService(
    private val firebaseMessaging: FirebaseMessaging,
) {
    fun sendPushNotification(
        fcmToken: String,
        title: String,
        body: String,
        link: String,
        studentId: String = "20000000"
    ): PushResult {
        val fcmMessage = Message.builder()
            .putData("title", title)
            .putData("body", body.replace("\n", " "))
            .putData("link", link)
            .setAndroidConfig(
                AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .build()
            )
            .setToken(fcmToken)
            .build()

        return try {
            firebaseMessaging.send(fcmMessage)
            log.info { "(studentId=$studentId) FCM Message sent." }
            PushResult.Success
        } catch (e: FirebaseMessagingException) {
            resolveFailure(e, studentId)
        } catch (e: Exception) {
            // 자격 증명 갱신 실패 등 SDK가 FirebaseMessagingException으로 감싸지 않는 오류
            log.error(e) { "(studentId=$studentId) FCM send failed: ${e.message}" }
            PushResult.Retryable(e.javaClass.simpleName)
        }
    }

    private fun resolveFailure(e: FirebaseMessagingException, studentId: String): PushResult {
        val errorCode = e.messagingErrorCode

        return when (errorCode) {
            MessagingErrorCode.UNREGISTERED, MessagingErrorCode.SENDER_ID_MISMATCH -> {
                log.warn { "(studentId=$studentId) FCM token is no longer valid($errorCode). Clearing token." }
                PushResult.InvalidToken
            }

            MessagingErrorCode.UNAVAILABLE,
            MessagingErrorCode.INTERNAL,
            MessagingErrorCode.QUOTA_EXCEEDED,
            MessagingErrorCode.THIRD_PARTY_AUTH_ERROR -> {
                log.error { "(studentId=$studentId) FCM send failed temporarily($errorCode): ${e.message}" }
                PushResult.Retryable(errorCode.name)
            }

            // 에러 코드가 없는 경우는 전송 계층 오류에 가까우므로 재시도 대상으로 둔다
            null -> {
                log.error { "(studentId=$studentId) FCM send failed without error code: ${e.message}" }
                PushResult.Retryable(e.javaClass.simpleName)
            }

            // INVALID_ARGUMENT는 토큰 문제일 수도, 페이로드 문제일 수도 있어 토큰을 지우지 않는다
            else -> {
                log.error { "(studentId=$studentId) FCM send failed permanently($errorCode): ${e.message}" }
                PushResult.Permanent(errorCode.name)
            }
        }
    }
}
