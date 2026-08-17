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
    private val notificationPushOutboxService: NotificationPushOutboxService,
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

    /**
     * 알림을 저장하고 푸시 발송 대상을 대기열에 등록한다. 등록된 아웃박스 ID를 반환한다.
     *
     * 알림과 발송 대상이 하나의 트랜잭션에서 저장되므로, 커밋된 이후에는 발송이 누락되더라도
     * 대기열에 남아 재시도된다.
     */
    @Transactional
    fun createNotification(
        member: Member,
        status: NotificationStatus,
        formatValues: List<String>,
        pushReceivers: List<Member> = emptyList(),
    ): List<Long> {
        val notification = notificationRepository.save(
            Notification(
                member = member,
                status = status,
                formatValues = formatValues.joinToString(",")
            )
        )

        return notificationPushOutboxService.register(notification, pushReceivers)
    }

    @Transactional
    fun createAdminNotification(
        status: NotificationStatus,
        formatValues: List<String>,
        pushReceivers: List<Member> = emptyList(),
    ): List<Long> {
        val notification = notificationRepository.save(
            Notification(
                status = status,
                formatValues = formatValues.joinToString(",")
            )
        )

        return notificationPushOutboxService.register(notification, pushReceivers)
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
