package site.billilge.api.backend.domain.notification.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.notification.dto.response.NotificationDetail
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.repository.NotificationRepository

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationRepository: NotificationRepository
) {
    fun getNotifications(memberId: Long?): NotificationFindAllResponse {
        val notifications = notificationRepository.findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(memberId!!)

        return NotificationFindAllResponse(notifications
            .map { NotificationDetail.from(it) })
    }
}