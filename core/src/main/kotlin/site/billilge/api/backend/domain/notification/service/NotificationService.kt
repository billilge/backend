package site.billilge.api.backend.domain.notification.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.core.port.FCMPort
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.notification.appender.NotificationAppender
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.exception.NotificationErrorCode
import site.billilge.api.backend.domain.notification.reader.NotificationReader

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationReader: NotificationReader,
    private val notificationAppender: NotificationAppender,
    private val fcmPort: FCMPort,
    private val memberService: MemberService,
) {
    fun getNotifications(memberId: Long): List<Notification> =
        notificationReader.readAllUserByMemberId(memberId)

    fun getAdminNotifications(memberId: Long): List<Notification> =
        notificationReader.readAllAdminByMemberId(memberId)

    fun getNotificationCount(memberId: Long): Int =
        notificationReader.countUnreadByMemberId(memberId)

    @Transactional
    fun readNotification(memberId: Long, notificationId: Long) {
        val notification = notificationReader.read(notificationId)
        if (notification.status.name.contains("ADMIN", ignoreCase = true)) return
        if (notification.member?.id != memberId) throw ApiException(NotificationErrorCode.NOTIFICATION_ACCESS_DENIED)
        notificationAppender.save(notification.copy(isRead = true))
    }

    @Transactional
    fun readAllNotifications(memberId: Long) {
        val notifications = notificationReader.readAllUserByMemberId(memberId)
        notificationAppender.saveAll(notifications.filter { !it.isRead }.map { it.copy(isRead = true) })
    }

    @Transactional
    fun sendNotification(member: Member, status: NotificationStatus, formatValues: List<String>, needPush: Boolean = false) {
        notificationAppender.save(
            Notification(id = null, member = member, status = status, formatValues = formatValues.joinToString(","))
        )
        if (needPush) sendPushNotification(member, status, formatValues)
    }

    @Transactional
    fun sendNotificationToAdmin(status: NotificationStatus, formatValues: List<String>, needPush: Boolean = false) {
        notificationAppender.save(
            Notification(id = null, member = null, status = status, formatValues = formatValues.joinToString(","))
        )
        if (needPush) {
            memberService.findAllWorkers().forEach { sendPushNotification(it, status, formatValues) }
        }
    }

    private fun sendPushNotification(member: Member, status: NotificationStatus, formatValues: List<String>) {
        val fcmToken = member.fcmToken
        if (fcmToken == null) {
            log.warn { "(studentId=${member.studentId}) FCM Token is null" }
            return
        }
        val isTokenValid = fcmPort.sendPushNotification(
            fcmToken = fcmToken,
            title = status.title,
            body = status.formattedMessage(*formatValues.toTypedArray()),
            link = status.link,
            studentId = member.studentId,
        )
        if (!isTokenValid) memberService.clearFcmToken(member.id!!)
    }
}
