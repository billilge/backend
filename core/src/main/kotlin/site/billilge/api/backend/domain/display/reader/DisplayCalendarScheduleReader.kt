package site.billilge.api.backend.domain.display.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import site.billilge.api.backend.domain.display.exception.DisplayCalendarScheduleErrorCode
import site.billilge.api.backend.domain.display.repository.DisplayCalendarScheduleRepository
import java.time.LocalDate

@Component
class DisplayCalendarScheduleReader(private val displayCalendarScheduleRepository: DisplayCalendarScheduleRepository) {

    fun read(id: Long): DisplayCalendarSchedule =
        displayCalendarScheduleRepository.findById(id)
            ?: throw ApiException(DisplayCalendarScheduleErrorCode.SCHEDULE_NOT_FOUND)

    fun readByDateBetween(startDate: LocalDate, endDate: LocalDate): List<DisplayCalendarSchedule> =
        displayCalendarScheduleRepository.findByDateBetween(startDate, endDate)
}
