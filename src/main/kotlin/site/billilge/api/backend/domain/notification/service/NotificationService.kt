package site.billilge.api.backend.domain.notification.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.notification.dto.response.NotificationDetail
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.exception.NotificationErrorCode
import site.billilge.api.backend.domain.notification.repository.NotificationRepository
import site.billilge.api.backend.global.exception.ApiException

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

    @Transactional
    fun readNotification(memberId: Long?, notificationId: Long) {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { ApiException(NotificationErrorCode.NOTIFICATION_NOT_FOUND) }

        if (notification.member.id != memberId) {
            throw ApiException(NotificationErrorCode.NOTIFICATION_ACCESS_DENIED)
        }

        notification.readNotification()
        notificationRepository.save(notification)
    }
}