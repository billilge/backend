package site.billilge.api.backend.domain.display.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import java.time.LocalDate

@Schema
data class DisplayCalendarScheduleFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = DisplayCalendarScheduleDetail::class))
    val schedules: List<DisplayCalendarScheduleDetail>,
) {
    @Schema
    data class DisplayCalendarScheduleDetail(
        @field:Schema(description = "일정 ID", example = "1")
        val scheduleId: Long,
        @field:Schema(description = "일정 날짜", example = "2025-03-15")
        val date: LocalDate,
        @field:Schema(description = "일정 목록", example = "[\"학생회 회의\", \"동아리 박람회\"]")
        val schedules: List<String>,
    ) {
        companion object {
            @JvmStatic
            fun from(schedule: DisplayCalendarSchedule): DisplayCalendarScheduleDetail {
                return DisplayCalendarScheduleDetail(
                    scheduleId = schedule.id!!,
                    date = schedule.date,
                    schedules = schedule.scheduleList,
                )
            }
        }
    }
}
