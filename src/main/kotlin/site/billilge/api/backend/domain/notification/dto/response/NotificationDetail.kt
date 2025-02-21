package site.billilge.api.backend.domain.notification.dto.response

import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import java.time.LocalDateTime

data class NotificationDetail(
    val notificationId: Long,
    val status: NotificationStatus,
    val title: String,
    val message: String,
    val link: String,
    val isRead: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        @JvmStatic
        fun from(notification: Notification): NotificationDetail {
            val status = notification.status

            return NotificationDetail(
                notificationId = notification.id!!,
                status = status,
                title = status.title,
                message = status.formattedMessage(*notification.formatValueList.toTypedArray()),
                link = status.link,
                isRead = notification.isRead,
                createdAt = notification.createdAt
            )
        }
    }
}