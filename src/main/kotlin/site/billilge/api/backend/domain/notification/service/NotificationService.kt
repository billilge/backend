package site.billilge.api.backend.domain.notification.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.exception.NotificationErrorCode
import site.billilge.api.backend.domain.notification.repository.NotificationRepository
import site.billilge.api.backend.global.exception.ApiException

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationRepository: NotificationRepository,
) {
    fun getNotifications(memberId: Long?): List<Notification> {
        return notificationRepository.findAllUserNotificationsByMemberId(memberId!!)
    }

    @Transactional
    fun readNotification(memberId: Long?, notificationId: Long) {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { ApiException(NotificationErrorCode.NOTIFICATION_NOT_FOUND) }

        if (notification.isAdminStatus()) return;

        if (notification.member?.id != memberId) {
            throw ApiException(NotificationErrorCode.NOTIFICATION_ACCESS_DENIED)
        }

        notification.readNotification()
    }

    fun getAdminNotifications(memberId: Long?): List<Notification> {
        return notificationRepository.findAllAdminNotificationsByMemberId(memberId!!)
    }

    @Transactional
    fun createNotification(
        member: Member,
        status: NotificationStatus,
        formatValues: List<String>,
    ): Notification {
        val notification = Notification(
            member = member,
            status = status,
            formatValues = formatValues.joinToString(",")
        )

        return notificationRepository.save(notification)
    }

    @Transactional
    fun createAdminNotification(
        status: NotificationStatus,
        formatValues: List<String>,
    ): Notification {
        val notification = Notification(
            status = status,
            formatValues = formatValues.joinToString(",")
        )

        return notificationRepository.save(notification)
    }

    fun getNotificationCount(memberId: Long?): Int {
        return notificationRepository.countUserNotificationsByMemberId(memberId!!)
    }

    @Transactional
    fun readAllNotifications(memberId: Long?) {
        notificationRepository
            .findAllUserNotificationsByMemberId(memberId!!)
            .forEach { it.readNotification() }
    }

    private fun Notification.isAdminStatus(): Boolean = status.name.contains("ADMIN", true)
}
