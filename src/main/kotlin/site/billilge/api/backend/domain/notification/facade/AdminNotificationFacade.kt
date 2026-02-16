package site.billilge.api.backend.domain.notification.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.notification.dto.response.NotificationDetail
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.service.NotificationService

@Component
class AdminNotificationFacade(
    private val notificationService: NotificationService,
) {
    fun getAdminNotifications(memberId: Long?): NotificationFindAllResponse {
        val notifications = notificationService.getAdminNotifications(memberId)
        return NotificationFindAllResponse(
            notifications.map { NotificationDetail.from(it) }
        )
    }
}
