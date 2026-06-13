package site.billilge.api.backend.domain.display.remover

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.display.repository.DisplayCalendarScheduleRepository

@Component
class DisplayCalendarScheduleRemover(private val displayCalendarScheduleRepository: DisplayCalendarScheduleRepository) {

    fun remove(id: Long) = displayCalendarScheduleRepository.delete(id)
}
