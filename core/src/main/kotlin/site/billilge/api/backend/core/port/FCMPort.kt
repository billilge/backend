package site.billilge.api.backend.core.port

interface FCMPort {
    fun sendPushNotification(
        fcmToken: String,
        title: String,
        body: String,
        link: String,
        studentId: String = "20000000",
    ): Boolean
}
