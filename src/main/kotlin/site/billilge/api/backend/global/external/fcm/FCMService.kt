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
            .setToken(fcmToken)
            .build()

        try {
            firebaseMessaging.sendAsync(fcmMessage)
            log.info { "(studentId=${studentId}) FCM Message sent." }
        } catch(exception: FirebaseMessagingException) {
            if (exception.messagingErrorCode == MessagingErrorCode.UNREGISTERED) {
                log.error { "(studentId=${studentId}) FCM token is unregistered." }
            }
        }
    }
}