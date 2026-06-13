package site.billilge.api.backend.domain.notification.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.repository.NotificationRepository

@Component
class NotificationAppender(private val notificationRepository: NotificationRepository) {

    fun save(notification: Notification): Notification = notificationRepository.save(notification)

    fun saveAll(notifications: List<Notification>): List<Notification> =
        notificationRepository.saveAll(notifications)
}
