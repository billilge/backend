package site.billilge.api.backend.domain.notification.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.notification.dto.response.NotificationCountResponse
import site.billilge.api.backend.domain.notification.dto.response.NotificationDetail
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.service.NotificationService

@Component
class NotificationFacade(
    private val notificationService: NotificationService,
) {
    fun getNotifications(memberId: Long?): NotificationFindAllResponse {
        val notifications = notificationService.getNotifications(memberId)
        return NotificationFindAllResponse(
            notifications.map { NotificationDetail.from(it) }
        )
    }

    fun getNotificationCount(memberId: Long?): NotificationCountResponse {
        val count = notificationService.getNotificationCount(memberId)
        return NotificationCountResponse(count)
    }

    fun readNotification(memberId: Long?, notificationId: Long) {
        notificationService.readNotification(memberId, notificationId)
    }

    fun readAllNotifications(memberId: Long?) {
        notificationService.readAllNotifications(memberId)
    }
}
