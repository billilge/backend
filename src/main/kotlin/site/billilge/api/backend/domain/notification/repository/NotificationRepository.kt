package site.billilge.api.backend.domain.notification.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.notification.entity.Notification

interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(memberId: Long): List<Notification>
}