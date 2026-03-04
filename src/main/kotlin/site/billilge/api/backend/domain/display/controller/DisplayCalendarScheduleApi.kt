package site.billilge.api.backend.domain.display.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.display.dto.request.DisplayCalendarScheduleRequest
import site.billilge.api.backend.domain.display.dto.response.DisplayCalendarScheduleFindAllResponse
import site.billilge.api.backend.global.exception.ErrorResponse
import java.time.LocalDate

@Tag(name = "(Admin) Display Calendar Schedule", description = "관리자용 전광판 일정 API")
interface DisplayCalendarScheduleApi {

    @Operation(
        summary = "기간별 일정 조회",
        description = "시작일과 종료일 사이의 일정 목록을 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "일정 조회 성공"
            )
        ]
    )
    fun getSchedules(
        @Parameter(description = "조회 시작일", example = "2025-03-01")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @Parameter(description = "조회 종료일", example = "2025-03-31")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<DisplayCalendarScheduleFindAllResponse>

    @Operation(
        summary = "일정 추가",
        description = "해당일 일정을 추가하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "일정 추가 성공"
            )
        ]
    )
    fun addSchedule(
        @RequestBody request: DisplayCalendarScheduleRequest
    ): ResponseEntity<Void>

    @Operation(
        summary = "일정 수정",
        description = "해당일 일정을 수정하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "일정 수정 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "일정을 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun updateSchedule(
        @PathVariable id: Long,
        @RequestBody request: DisplayCalendarScheduleRequest
    ): ResponseEntity<Void>

    @Operation(
        summary = "일정 삭제",
        description = "해당일 일정을 삭제하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "일정 삭제 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "일정을 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun deleteSchedule(
        @PathVariable id: Long
    ): ResponseEntity<Void>
}
