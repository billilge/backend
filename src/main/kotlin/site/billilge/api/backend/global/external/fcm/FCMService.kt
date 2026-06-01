package site.billilge.api.backend.global.external.fcm

import com.google.firebase.messaging.*
import org.springframework.stereotype.Service
import site.billilge.api.backend.global.logging.log

@Service
class FCMService(
    private val firebaseMessaging: FirebaseMessaging,
) {
    fun sendPushNotification(fcmToken: String, title: String, body: String, link: String, studentId: String = "20000000") {
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

        try {
            firebaseMessaging.send(fcmMessage)
            log.info { "(studentId=$studentId) FCM Message sent." }
        } catch (e: FirebaseMessagingException) {
            if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED) {
                log.error { "(studentId=$studentId) FCM token is unregistered." }
            } else {
                log.error { "(studentId=$studentId) FCM send failed: ${e.message}" }
            }
        }
    }
}