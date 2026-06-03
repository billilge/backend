package site.billilge.api.backend.domain.notification.handler

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.service.NotificationService
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.event.*

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService,
    private val memberService: MemberService,
) {
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleRentalApplied(event: RentalAppliedEvent) {
        val member = memberService.findById(event.memberId)

        notificationService.sendNotification(
            member,
            NotificationStatus.USER_RENTAL_APPLY,
            listOf(event.itemName),
            needPush = true,
        )

        if (!event.isDevMode) {
            notificationService.sendNotificationToAdmin(
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleRentalCancelled(event: RentalCancelledEvent) {
        val member = memberService.findById(event.memberId)

        notificationService.sendNotificationToAdmin(
            NotificationStatus.ADMIN_RENTAL_CANCEL,
            listOf(member.name, member.studentId, event.itemName),
            needPush = true,
        )
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleReturnApplied(event: ReturnAppliedEvent) {
        val member = memberService.findById(event.memberId)

        notificationService.sendNotification(
            member,
            NotificationStatus.USER_RETURN_APPLY,
            listOf(event.itemName),
            needPush = true,
        )

        notificationService.sendNotificationToAdmin(
            NotificationStatus.ADMIN_RETURN_APPLY,
            listOf(member.name, member.studentId, event.itemName),
            needPush = true,
        )
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

        notificationService.sendNotification(member, notificationStatus, listOf(event.itemName), needPush)
    }
}
