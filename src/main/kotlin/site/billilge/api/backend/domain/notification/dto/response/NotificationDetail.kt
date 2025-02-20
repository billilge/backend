package site.billilge.api.backend.domain.notification.dto.response

import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.enums.NotificationType
import java.time.LocalDateTime

data class NotificationDetail(
    val notificationId: Long,
    val type: NotificationType,
    val message: String,
    val link: String,
    val isRead: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(notification: Notification): NotificationDetail {
            return NotificationDetail(
                notificationId = notification.id!!,
                type = notification.type,
                message = notification.message,
                link = notification.link,
                isRead = notification.isRead,
                createdAt = notification.createdAt
            )
        }
    }
}