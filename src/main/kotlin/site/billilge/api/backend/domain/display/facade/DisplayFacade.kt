package site.billilge.api.backend.domain.display.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.display.dto.response.DisplayResponse
import site.billilge.api.backend.domain.display.service.DisplayCalendarScheduleService
import site.billilge.api.backend.domain.display.service.DisplayPosterService
import site.billilge.api.backend.domain.item.service.ItemService
import java.time.LocalDate

@Component
class DisplayFacade(
    private val displayPosterService: DisplayPosterService,
    private val displayCalendarScheduleService: DisplayCalendarScheduleService,
    private val itemService: ItemService,
) {
    fun getDisplay(): DisplayResponse {
        val posters = displayPosterService.getActivePosters()
            .map { poster ->
                DisplayResponse.PosterDetail(
                    posterId = poster.id!!,
                    title = poster.title,
                    imageUrl = poster.imageUrl,
                )
            }

        val today = LocalDate.now()
        val startDate = today.minusDays(3)
        val endDate = today.plusDays(3)

        val scheduleEntities = displayCalendarScheduleService.getSchedules(startDate, endDate)
        val scheduleMap = scheduleEntities.associate { it.date to it.scheduleList }

        val schedules = (0L..6L).map { offset ->
            val date = startDate.plusDays(offset)
            mapOf(date.toString() to (scheduleMap[date] ?: emptyList()))
        }

        val items = itemService.getAllItems()
            .map { item ->
                DisplayResponse.DisplayItemDetail(
                    itemName = item.name,
                    count = item.count,
                    imageUrl = item.imageUrl,
                )
            }

        return DisplayResponse(
            posters = posters,
            schedules = schedules,
            items = items,
        )
    }
}
