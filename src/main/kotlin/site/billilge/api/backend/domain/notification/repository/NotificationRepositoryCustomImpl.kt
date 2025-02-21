package site.billilge.api.backend.domain.notification.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import site.billilge.api.backend.domain.notification.entity.Notification
import site.billilge.api.backend.domain.notification.entity.QNotification
import site.billilge.api.backend.domain.notification.enums.NotificationStatus

class NotificationRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : NotificationRepositoryCustom {
    override fun findAllUserNotificationsByMemberId(memberId: Long): List<Notification> {
        val notification = QNotification.notification

        return queryFactory
            .select(notification)
            .from(notification)
            .where(
                memberIdEquals(notification, memberId)
                    .and(
                        notification.status.`in`(USER_TYPE)
                    )
            )
            .orderBy(notification.createdAt.desc())
            .fetch()
    }

    override fun findAllAdminNotificationsByMemberId(memberId: Long): List<Notification> {
        val notification = QNotification.notification

        return queryFactory
            .select(notification)
            .from(notification)
            .where(
                memberIdEquals(notification, memberId)
                    .and(
                        notification.status.`in`(ADMIN_TYPE)
                    )
            )
            .orderBy(notification.createdAt.desc())
            .fetch()
    }

    override fun countUserNotificationsByMemberId(memberId: Long): Int {
        val notification = QNotification.notification

        return queryFactory
            .select(notification.count())
            .from(notification)
            .where(
                memberIdEquals(notification, memberId)
                    .and(
                        notification.status.`in`(USER_TYPE)
                    )
            )
            .fetchOne()?.toInt() ?: 0
    }

    private fun memberIdEquals(notification: QNotification, memberId: Long): BooleanExpression {
        return notification.member.id.eq(memberId)
    }

    companion object {
        private val USER_TYPE = listOf(
            NotificationStatus.USER_RENTAL_APPLY,
            NotificationStatus.USER_RENTAL_APPROVED,
            NotificationStatus.USER_RENTAL_REJECTED,
            NotificationStatus.USER_RETURN_APPLY,
            NotificationStatus.USER_RETURN_COMPLETED
        )

        private val ADMIN_TYPE = listOf(
            NotificationStatus.ADMIN_RENTAL_APPLY,
            NotificationStatus.ADMIN_RENTAL_CANCEL,
            NotificationStatus.ADMIN_RETURN_APPLY,
            NotificationStatus.ADMIN_RETURN_CANCEL
        )
    }
}