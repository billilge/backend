package site.billilge.api.backend.domain.notification.repository

import site.billilge.api.backend.domain.notification.entity.Notification

interface NotificationRepositoryCustom {
    fun findAllUserNotificationsByMemberId(memberId: Long): List<Notification>
    fun findAllAdminNotificationsByMemberId(memberId: Long): List<Notification>
    fun countUserNotificationsByMemberId(memberId: Long): Int
}