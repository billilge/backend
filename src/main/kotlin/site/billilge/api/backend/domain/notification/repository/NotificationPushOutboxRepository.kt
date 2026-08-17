package site.billilge.api.backend.domain.notification.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.billilge.api.backend.domain.notification.entity.NotificationPushOutbox
import site.billilge.api.backend.domain.notification.enums.PushDeliveryStatus
import java.time.LocalDateTime

interface NotificationPushOutboxRepository : JpaRepository<NotificationPushOutbox, Long> {
    @Query(
        """
        SELECT o.id FROM NotificationPushOutbox o
        WHERE o.deliveryStatus = :deliveryStatus AND o.nextRetryAt <= :now
        ORDER BY o.nextRetryAt ASC
        """
    )
    fun findDispatchTargetIds(
        deliveryStatus: PushDeliveryStatus,
        now: LocalDateTime,
        pageable: Pageable
    ): List<Long>

    /** 정렬하지 않는다 — 보존 기간이 지난 건을 모두 지우므로 삭제 순서는 중요하지 않다 */
    @Query(
        """
        SELECT o.id FROM NotificationPushOutbox o
        WHERE o.deliveryStatus IN :deliveryStatuses AND o.createdAt < :createdBefore
        """
    )
    fun findPurgeTargetIds(
        deliveryStatuses: Collection<PushDeliveryStatus>,
        createdBefore: LocalDateTime,
        pageable: Pageable
    ): List<Long>
}
