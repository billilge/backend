package site.billilge.api.backend.global.external.fcm

import com.google.firebase.messaging.*
import org.springframework.stereotype.Service

@Service
class FCMService(
    private val firebaseMessaging: FirebaseMessaging,
) {
    @Throws(FirebaseMessagingException::class)
    fun sendPushNotification(fcmToken: String, title: String, body: String, link: String) {
        val fcmMessage = Message.builder()
            .putData("title", title)
            .putData("body", body.replace("\n", " "))
            .putData("link", link)
            .setToken(fcmToken)
            .build()

        firebaseMessaging.send(fcmMessage)
    }
}