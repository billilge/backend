package site.billilge.api.backend.global.utils

import java.time.DayOfWeek
import java.time.LocalDateTime

val LocalDateTime.isWeekend: Boolean
    get() = this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY