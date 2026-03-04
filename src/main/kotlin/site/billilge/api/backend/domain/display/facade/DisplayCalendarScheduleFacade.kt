package site.billilge.api.backend.domain.display.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.display.dto.request.DisplayCalendarScheduleRequest
import site.billilge.api.backend.domain.display.dto.response.DisplayCalendarScheduleFindAllResponse
import site.billilge.api.backend.domain.display.dto.response.DisplayCalendarScheduleFindAllResponse.DisplayCalendarScheduleDetail
import site.billilge.api.backend.domain.display.service.DisplayCalendarScheduleService
import java.time.LocalDate

@Component
class DisplayCalendarScheduleFacade(
    private val displayCalendarScheduleService: DisplayCalendarScheduleService,
) {
    fun getSchedules(startDate: LocalDate, endDate: LocalDate): DisplayCalendarScheduleFindAllResponse {
        val schedules = displayCalendarScheduleService.getSchedules(startDate, endDate)
        return DisplayCalendarScheduleFindAllResponse(
            schedules.map { DisplayCalendarScheduleDetail.from(it) }
        )
    }

    fun addSchedule(request: DisplayCalendarScheduleRequest) {
        displayCalendarScheduleService.addSchedule(request.date, request.schedules)
    }

    fun updateSchedule(scheduleId: Long, request: DisplayCalendarScheduleRequest) {
        displayCalendarScheduleService.updateSchedule(scheduleId, request.date, request.schedules)
    }

    fun deleteSchedule(scheduleId: Long) {
        displayCalendarScheduleService.deleteSchedule(scheduleId)
    }
}
