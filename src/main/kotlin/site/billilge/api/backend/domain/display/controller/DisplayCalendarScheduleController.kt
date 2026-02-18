package site.billilge.api.backend.domain.display.controller

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.display.dto.request.DisplayCalendarScheduleRequest
import site.billilge.api.backend.domain.display.dto.response.DisplayCalendarScheduleFindAllResponse
import site.billilge.api.backend.domain.display.facade.DisplayCalendarScheduleFacade
import site.billilge.api.backend.global.annotation.OnlyAdmin
import java.time.LocalDate

@RestController
@RequestMapping("/display/calendar-schedules")
@OnlyAdmin
class DisplayCalendarScheduleController(
    private val displayCalendarScheduleFacade: DisplayCalendarScheduleFacade,
) : DisplayCalendarScheduleApi {
    @GetMapping
    override fun getSchedules(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<DisplayCalendarScheduleFindAllResponse> {
        return ResponseEntity.ok(displayCalendarScheduleFacade.getSchedules(startDate, endDate))
    }

    @PostMapping
    override fun addSchedule(
        @RequestBody request: DisplayCalendarScheduleRequest
    ): ResponseEntity<Void> {
        displayCalendarScheduleFacade.addSchedule(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    override fun updateSchedule(
        @PathVariable id: Long,
        @RequestBody request: DisplayCalendarScheduleRequest
    ): ResponseEntity<Void> {
        displayCalendarScheduleFacade.updateSchedule(id, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    override fun deleteSchedule(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        displayCalendarScheduleFacade.deleteSchedule(id)
        return ResponseEntity.noContent().build()
    }
}
