package site.billilge.api.backend.domain.notification.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.enums.PushDeliveryStatus
import java.time.LocalDateTime

class NotificationPushOutboxTest {
    @Test
    @DisplayName("생성 직후에는 즉시 발송 시도와 겹치지 않도록 폴러 대상 시각이 뒤로 밀려 있다")
    fun `첫 폴링 시각은 생성 시각보다 뒤에 있다`() {
        val outbox = createOutbox()

        assertTrue(outbox.nextRetryAt.isAfter(outbox.createdAt))
        assertEquals(PushDeliveryStatus.PENDING, outbox.deliveryStatus)
        assertEquals(0, outbox.retryCount)
    }

    @Test
    @DisplayName("재시도 가능한 실패는 정해진 간격만큼 다음 시도를 미룬다")
    fun `백오프 간격이 점점 늘어난다`() {
        val outbox = createOutbox()
        val now = LocalDateTime.now()

        val expectedBackoffSeconds = listOf(30L, 120L, 300L, 900L)

        expectedBackoffSeconds.forEachIndexed { index, seconds ->
            outbox.recordRetryableFailure("UNAVAILABLE", now)

            assertEquals(now.plusSeconds(seconds), outbox.nextRetryAt)
            assertEquals(index + 1, outbox.retryCount)
            assertEquals(PushDeliveryStatus.PENDING, outbox.deliveryStatus)
        }
    }

    @Test
    @DisplayName("재시도 횟수를 모두 쓰면 발송을 포기한다")
    fun `최대 재시도 횟수를 넘기면 FAILED가 된다`() {
        val outbox = createOutbox()
        val now = LocalDateTime.now()

        repeat(4) { outbox.recordRetryableFailure("UNAVAILABLE", now) }
        assertEquals(PushDeliveryStatus.PENDING, outbox.deliveryStatus)

        outbox.recordRetryableFailure("UNAVAILABLE", now)

        assertEquals(PushDeliveryStatus.FAILED, outbox.deliveryStatus)
        assertEquals("UNAVAILABLE", outbox.lastError)
    }

    @Test
    @DisplayName("유효 시간이 지나면 재시도 횟수가 남아 있어도 포기한다")
    fun `TTL을 넘기면 EXPIRED가 된다`() {
        val outbox = createOutbox()

        outbox.recordRetryableFailure("UNAVAILABLE", LocalDateTime.now().plusHours(2))

        assertEquals(PushDeliveryStatus.EXPIRED, outbox.deliveryStatus)
        assertEquals(0, outbox.retryCount)
    }

    @Test
    @DisplayName("발송에 성공하면 직전 실패 기록을 지운다")
    fun `markSent는 lastError를 비운다`() {
        val outbox = createOutbox()
        outbox.recordRetryableFailure("UNAVAILABLE", LocalDateTime.now())

        outbox.markSent()

        assertEquals(PushDeliveryStatus.SENT, outbox.deliveryStatus)
        assertEquals(null, outbox.lastError)
    }

    @Test
    @DisplayName("처리가 끝난 건은 다시 발송 대상이 되지 않는다")
    fun `SENT 상태는 isPending이 false다`() {
        val outbox = createOutbox()

        outbox.markSent()

        assertTrue(!outbox.isPending())
    }

    private fun createOutbox(): NotificationPushOutbox {
        val member = Member(name = "김국민", studentId = "20240001")
        val notification = Notification(
            member = member,
            status = NotificationStatus.USER_RENTAL_APPROVED,
            formatValues = "우산"
        )

        return NotificationPushOutbox(notification, member)
    }
}
