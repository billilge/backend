package site.billilge.api.backend.domain.display.entity

import java.time.LocalDate

data class DisplayCalendarSchedule(
    val id: Long?,
    val date: LocalDate,
    val schedules: String,
) {
    val scheduleList: List<String>
        get() = schedules.split("|").map { it.trim() }.filter { it.isNotEmpty() }
}
