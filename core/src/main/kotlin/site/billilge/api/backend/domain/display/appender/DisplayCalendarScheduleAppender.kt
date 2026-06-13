package site.billilge.api.backend.domain.display.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import site.billilge.api.backend.domain.display.repository.DisplayCalendarScheduleRepository

@Component
class DisplayCalendarScheduleAppender(private val displayCalendarScheduleRepository: DisplayCalendarScheduleRepository) {

    fun save(schedule: DisplayCalendarSchedule): DisplayCalendarSchedule =
        displayCalendarScheduleRepository.save(schedule)
}
