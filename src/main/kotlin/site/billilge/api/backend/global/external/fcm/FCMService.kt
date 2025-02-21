package site.billilge.api.backend.global.external.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class FCMService(
    private val firebaseMessaging: FirebaseMessaging,
) {
    @Throws(FirebaseMessagingException::class)
    fun sendPushNotification(fcmToken: String, title: String, body: String) {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build()

        val fcmMessage = Message.builder()
            .setNotification(notification)
            .setToken(fcmToken)
            .build()

        firebaseMessaging.send(fcmMessage)
    }
}