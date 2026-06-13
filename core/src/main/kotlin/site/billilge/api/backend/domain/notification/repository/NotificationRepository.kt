package site.billilge.api.backend.domain.notification.repository

import site.billilge.api.backend.domain.notification.entity.Notification

interface NotificationRepository {
    fun findById(id: Long): Notification?
    fun save(notification: Notification): Notification
    fun saveAll(notifications: List<Notification>): List<Notification>
    fun findAllUserNotificationsByMemberId(memberId: Long): List<Notification>
    fun findAllAdminNotificationsByMemberId(memberId: Long): List<Notification>
    fun countUserNotificationsByMemberId(memberId: Long): Int
}
