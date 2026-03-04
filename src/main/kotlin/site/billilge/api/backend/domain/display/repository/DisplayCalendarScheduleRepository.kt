package site.billilge.api.backend.domain.display.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.display.entity.DisplayCalendarSchedule
import java.time.LocalDate

interface DisplayCalendarScheduleRepository : JpaRepository<DisplayCalendarSchedule, Long> {
    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate): List<DisplayCalendarSchedule>
}
