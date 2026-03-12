package site.billilge.api.backend.domain.notification.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.service.MemberService
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
    private val memberService: MemberService,
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
    fun sendNotification(
        member: Member,
        status: NotificationStatus,
        formatValues: List<String>,
        needPush: Boolean = false,
    ) {
        val notification = Notification(
            member = member,
            status = status,
            formatValues = formatValues.joinToString(",")
        )

        notificationRepository.save(notification)

        if (needPush) {
            sendPushNotification(member, status, formatValues)
        }
    }

    private fun sendPushNotification(
        member: Member,
        status: NotificationStatus,
        formatValues: List<String>,
    ) {
        val studentId = member.studentId

        if (member.fcmToken == null) {
            log.warn { "(studentId=${studentId}) FCM Token is null" }
            return
        }

        fcmService.sendPushNotification(
            member.fcmToken!!,
            status.title,
            status.formattedMessage(*formatValues.toTypedArray()),
            status.link,
            studentId
        )
    }

    @Transactional
    fun sendNotificationToAdmin(
        type: NotificationStatus,
        formatValues: List<String>,
        needPush: Boolean = false
    ) {
        val admins = memberService.findAllWorkers()

        val notification = Notification(
            status = type,
            formatValues = formatValues.joinToString(",")
        )

        notificationRepository.save(notification)

        if (needPush) {
            admins.forEach { admin ->
                sendPushNotification(admin, type, formatValues)
            }
        }
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
