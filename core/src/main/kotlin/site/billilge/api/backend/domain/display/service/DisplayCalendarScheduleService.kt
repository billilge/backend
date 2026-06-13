package site.billilge.api.backend.domain.display.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.display.appender.DisplayCalendarScheduleAppender
import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import site.billilge.api.backend.domain.display.reader.DisplayCalendarScheduleReader
import site.billilge.api.backend.domain.display.remover.DisplayCalendarScheduleRemover
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class DisplayCalendarScheduleService(
    private val displayCalendarScheduleReader: DisplayCalendarScheduleReader,
    private val displayCalendarScheduleAppender: DisplayCalendarScheduleAppender,
    private val displayCalendarScheduleRemover: DisplayCalendarScheduleRemover,
) {
    fun getSchedules(startDate: LocalDate, endDate: LocalDate): List<DisplayCalendarSchedule> =
        displayCalendarScheduleReader.readByDateBetween(startDate, endDate)

    @Transactional
    fun addSchedule(date: LocalDate, schedules: List<String>) {
        displayCalendarScheduleAppender.save(
            DisplayCalendarSchedule(id = null, date = date, schedules = schedules.joinToString("|"))
        )
    }

    @Transactional
    fun updateSchedule(scheduleId: Long, date: LocalDate, schedules: List<String>) {
        val schedule = displayCalendarScheduleReader.read(scheduleId)
        displayCalendarScheduleAppender.save(
            schedule.copy(date = date, schedules = schedules.joinToString("|"))
        )
    }

    @Transactional
    fun deleteSchedule(scheduleId: Long) {
        displayCalendarScheduleReader.read(scheduleId)
        displayCalendarScheduleRemover.remove(scheduleId)
    }
}
