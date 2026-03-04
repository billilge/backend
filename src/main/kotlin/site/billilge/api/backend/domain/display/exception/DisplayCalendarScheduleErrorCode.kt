package site.billilge.api.backend.domain.display.exception

import org.springframework.http.HttpStatus
import site.billilge.api.backend.global.exception.ErrorCode

enum class DisplayCalendarScheduleErrorCode(
    override val message: String,
    override val httpStatus: HttpStatus
): ErrorCode {
    SCHEDULE_NOT_FOUND("일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
}
