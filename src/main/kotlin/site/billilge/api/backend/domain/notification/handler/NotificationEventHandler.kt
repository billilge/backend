package site.billilge.api.backend.domain.notification.handler

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.service.NotificationService
import site.billilge.api.backend.domain.notification.service.PushNotificationSender
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.event.*

/**
 * 알림 저장(DB)과 푸시 발송(FCM)을 분리해 호출한다.
 *
 * 핸들러 자체에는 트랜잭션을 걸지 않는다. 알림 저장은 NotificationService의 짧은 트랜잭션에서
 * 즉시 커밋되고, 그 뒤 트랜잭션 밖에서 푸시를 보낸다. 푸시가 실패해도 저장된 알림은 남는다.
 */
@Component
class NotificationEventHandler(
    private val notificationService: NotificationService,
    private val pushNotificationSender: PushNotificationSender,
    private val memberService: MemberService,
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleRentalApplied(event: RentalAppliedEvent) {
        val member = memberService.findById(event.memberId)

        notifyUser(
            member,
            NotificationStatus.USER_RENTAL_APPLY,
            listOf(event.itemName),
            needPush = true,
        )

        if (!event.isDevMode) {
            notifyAdmins(
                NotificationStatus.ADMIN_RENTAL_APPLY,
                listOf(
                    member.name,
                    member.studentId,
                    "%02d:%02d".format(event.rentAt.hour, event.rentAt.minute),
                    event.itemName,
                ),
                needPush = true,
            )
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleRentalCancelled(event: RentalCancelledEvent) {
        val member = memberService.findById(event.memberId)

        notifyAdmins(
            NotificationStatus.ADMIN_RENTAL_CANCEL,
            listOf(member.name, member.studentId, event.itemName),
            needPush = true,
        )
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleReturnApplied(event: ReturnAppliedEvent) {
        val member = memberService.findById(event.memberId)

        notifyUser(
            member,
            NotificationStatus.USER_RETURN_APPLY,
            listOf(event.itemName),
            needPush = true,
        )

        notifyAdmins(
            NotificationStatus.ADMIN_RETURN_APPLY,
            listOf(member.name, member.studentId, event.itemName),
            needPush = true,
        )
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleRentalStatusChanged(event: RentalStatusChangedEvent) {
        val member = memberService.findById(event.memberId)

        val (notificationStatus, needPush) = when (event.status) {
            RentalStatus.CONFIRMED -> NotificationStatus.USER_RENTAL_APPROVED to true
            RentalStatus.REJECTED -> NotificationStatus.USER_RENTAL_REJECTED to true
            RentalStatus.RETURN_CONFIRMED -> NotificationStatus.USER_RETURN_APPROVED to true
            RentalStatus.RETURNED -> NotificationStatus.USER_RETURN_COMPLETED to false
            else -> return
        }

        notifyUser(member, notificationStatus, listOf(event.itemName), needPush)
    }

    private fun notifyUser(
        member: Member,
        status: NotificationStatus,
        formatValues: List<String>,
        needPush: Boolean,
    ) {
        val outboxIds = notificationService.createNotification(
            member,
            status,
            formatValues,
            pushReceivers = if (needPush) listOf(member) else emptyList(),
        )

        pushNotificationSender.dispatch(outboxIds)
    }

    private fun notifyAdmins(
        status: NotificationStatus,
        formatValues: List<String>,
        needPush: Boolean,
    ) {
        val outboxIds = notificationService.createAdminNotification(
            status,
            formatValues,
            pushReceivers = if (needPush) memberService.findAllWorkers() else emptyList(),
        )

        pushNotificationSender.dispatch(outboxIds)
    }
}
