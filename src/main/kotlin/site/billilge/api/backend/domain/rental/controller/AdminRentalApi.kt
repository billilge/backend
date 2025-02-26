package site.billilge.api.backend.domain.rental.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.rental.dto.request.RentalStatusUpdateRequest
import site.billilge.api.backend.domain.rental.dto.response.AdminRentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.DashboardResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Tag(name = "(Admin) Rental", description = "관리자용 대여 API")
interface AdminRentalApi {
    @Operation(
        summary = "관리자 대시보드 목록 조회",
        description = "관리자 대시보드 페이지 조회를 위한 API"
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "200",
                description = "대시보드 목록 조회 성공"
            )
        ]
    )
    fun getAllDashboardApplications(
        @RequestParam(required = false) rentalStatus: RentalStatus?,
    ): ResponseEntity<DashboardResponse>

    @Operation(
        summary = "모든 대여 기록 조회",
        description = "모든 대여 기록을 조회하기 위한 관리자용 API"
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "200",
                description = "대여 기록 조회 성공"
            )
        ]
    )
    fun getAllRentalHistories(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminRentalHistoryFindAllResponse>

    @Operation(
        summary = "대여 상태 변경",
        description = "대여 승인, 대여, 반납 승인, 반납 처리 등을 위해 대여 기록의 상태를 변경하는 관리자용 API"
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "200",
                description = "대여 상태 변경 성공"
            )
        ]
    )
    fun updateRentalStatus(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long,
        @RequestBody request: RentalStatusUpdateRequest
    ): ResponseEntity<Void>
}