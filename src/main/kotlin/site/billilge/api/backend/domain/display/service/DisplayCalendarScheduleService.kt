package site.billilge.api.backend.domain.display.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import site.billilge.api.backend.domain.display.exception.DisplayCalendarScheduleErrorCode
import site.billilge.api.backend.domain.display.repository.DisplayCalendarScheduleRepository
import site.billilge.api.backend.global.exception.ApiException
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class DisplayCalendarScheduleService(
    private val displayCalendarScheduleRepository: DisplayCalendarScheduleRepository,
) {
    fun getSchedules(startDate: LocalDate, endDate: LocalDate): List<DisplayCalendarSchedule> {
        return displayCalendarScheduleRepository.findByDateBetween(startDate, endDate)
    }

    @Transactional
    fun addSchedule(date: LocalDate, schedules: List<String>) {
        val schedule = DisplayCalendarSchedule(
            date = date,
            schedules = schedules.joinToString("|"),
        )
        displayCalendarScheduleRepository.save(schedule)
    }

    @Transactional
    fun updateSchedule(scheduleId: Long, date: LocalDate, schedules: List<String>) {
        val schedule = displayCalendarScheduleRepository.findById(scheduleId)
            .orElseThrow { ApiException(DisplayCalendarScheduleErrorCode.SCHEDULE_NOT_FOUND) }

        schedule.update(date, schedules)
    }

    @Transactional
    fun deleteSchedule(scheduleId: Long) {
        val schedule = displayCalendarScheduleRepository.findById(scheduleId)
            .orElseThrow { ApiException(DisplayCalendarScheduleErrorCode.SCHEDULE_NOT_FOUND) }

        displayCalendarScheduleRepository.delete(schedule)
    }
}
