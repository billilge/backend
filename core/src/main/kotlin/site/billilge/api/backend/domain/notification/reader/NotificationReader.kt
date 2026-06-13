package site.billilge.api.backend.domain.notification.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.exception.NotificationErrorCode
import site.billilge.api.backend.domain.notification.repository.NotificationRepository

@Component
class NotificationReader(private val notificationRepository: NotificationRepository) {

    fun read(id: Long): Notification =
        notificationRepository.findById(id) ?: throw ApiException(NotificationErrorCode.NOTIFICATION_NOT_FOUND)

    fun readAllUserByMemberId(memberId: Long): List<Notification> =
        notificationRepository.findAllUserNotificationsByMemberId(memberId)

    fun readAllAdminByMemberId(memberId: Long): List<Notification> =
        notificationRepository.findAllAdminNotificationsByMemberId(memberId)

    fun countUnreadByMemberId(memberId: Long): Int =
        notificationRepository.countUserNotificationsByMemberId(memberId)
}
