package site.billilge.api.backend.domain.display.exception

import site.billilge.api.backend.common.exception.ErrorCode

enum class DisplayCalendarScheduleErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    SCHEDULE_NOT_FOUND("일정을 찾을 수 없습니다.", 404),
}
