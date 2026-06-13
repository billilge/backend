package site.billilge.api.backend.domain.display.repository

import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import java.time.LocalDate

interface DisplayCalendarScheduleRepository {
    fun findById(id: Long): DisplayCalendarSchedule?
    fun findAll(): List<DisplayCalendarSchedule>
    fun save(schedule: DisplayCalendarSchedule): DisplayCalendarSchedule
    fun delete(id: Long)
    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate): List<DisplayCalendarSchedule>
}
