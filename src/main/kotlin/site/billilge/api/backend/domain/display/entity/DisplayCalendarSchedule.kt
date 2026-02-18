package site.billilge.api.backend.domain.display.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "display_calendar_schedule")
class DisplayCalendarSchedule(
    @Column(nullable = false)
    var date: LocalDate,

    @Column(nullable = false, columnDefinition = "TEXT")
    var schedules: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val scheduleList: List<String>
        get() = schedules.split("|").map { it.trim() }.filter { it.isNotEmpty() }

    fun update(date: LocalDate, schedules: List<String>) {
        this.date = date
        this.schedules = schedules.joinToString("|")
    }
}
