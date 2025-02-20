package site.billilge.api.backend.domain.notification.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class NotificationErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    NOTIFICATION_NOT_FOUND("알림 정보를 찾을 없습니다.", HttpStatus.NOT_FOUND),
    NOTIFICATION_ACCESS_DENIED("알림을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN)
}