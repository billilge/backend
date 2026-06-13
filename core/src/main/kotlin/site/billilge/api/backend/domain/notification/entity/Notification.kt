package site.billilge.api.backend.domain.notification.entity

import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import java.time.LocalDateTime

data class Notification(
    val id: Long?,
    val member: Member?,
    val status: NotificationStatus,
    val formatValues: String? = null,
    val isRead: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    val formatValueList: List<String>
        get() = formatValues?.split(",") ?: emptyList()
}
