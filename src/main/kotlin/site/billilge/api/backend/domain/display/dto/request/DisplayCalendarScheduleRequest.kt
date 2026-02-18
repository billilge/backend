package site.billilge.api.backend.domain.display.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema
data class DisplayCalendarScheduleRequest(
    @field:Schema(description = "일정 날짜", example = "2025-03-15")
    val date: LocalDate,
    @field:Schema(description = "일정 목록", example = "[\"학생회 회의\", \"동아리 박람회\"]")
    val schedules: List<String>,
)
