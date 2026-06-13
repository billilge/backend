package site.billilge.api.backend.domain.notification.exception

import site.billilge.api.backend.common.exception.ErrorCode

enum class NotificationErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    NOTIFICATION_NOT_FOUND("알림 정보를 찾을 수 없습니다.", 404),
    NOTIFICATION_ACCESS_DENIED("알림을 수정할 권한이 없습니다.", 403),
}
