package site.billilge.api.backend.domain.notification.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.notification.dto.response.NotificationCountResponse
import site.billilge.api.backend.domain.notification.dto.response.NotificationDetail
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.exception.NotificationErrorCode
import site.billilge.api.backend.domain.notification.repository.NotificationRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.external.fcm.FCMService

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val fcmService: FCMService,
    private val memberRepository: MemberRepository,
) {
    fun getNotifications(memberId: Long?): NotificationFindAllResponse {
        val notifications = notificationRepository.findAllUserNotificationsByMemberId(memberId!!)

        return NotificationFindAllResponse(
            notifications
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
    }

    fun getAdminNotifications(memberId: Long?): NotificationFindAllResponse {
        val notifications = notificationRepository.findAllAdminNotificationsByMemberId(memberId!!)

        return NotificationFindAllResponse(
            notifications
                .map { NotificationDetail.from(it) })
    }

    @Transactional
    fun sendNotification(
        member: Member,
        status: NotificationStatus,
        formatValues: List<String>,
        needPush: Boolean = false
    ) {
        val notification = Notification(
            member = member,
            status = status,
            formatValues = formatValues.joinToString(",")
        )

        notificationRepository.save(notification)

        if (needPush) {
            if (member.fcmToken == null) {
                log.warn { "(studentId=${member.studentId}) FCM Token is null" }
                return
            }

            fcmService.sendPushNotification(member.fcmToken!!, status.title, status.formattedMessage(*formatValues.toTypedArray()))
        }
    }

    fun sendNotificationToAdmin(
        type: NotificationStatus,
        formatValues: List<String>,
        needPush: Boolean = false
    ) {
        val admins = memberRepository.findAllByRole(Role.ADMIN)

        admins.forEach { admin ->
            sendNotification(admin, type, formatValues, needPush)
        }
    }

    fun getNotificationCount(memberId: Long?): NotificationCountResponse {
        val count = notificationRepository.countUserNotificationsByMemberId(memberId!!)

        return NotificationCountResponse(count)
    }
}