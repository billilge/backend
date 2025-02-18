package site.billilge.api.backend.domain.rental.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.rental.dto.request.RentalRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryDetail
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Tag(name = "Rental", description = "대여 API")
interface RentalApi {
    @Operation(
        summary = "물품 대여 신청",
        description = "물품을 대여 신청하는 사용자용 API"
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "201",
                description = "대여 성공"
            )
        ]
    )
    fun createRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody rentalRequest: RentalRequest): ResponseEntity<Void>

    @Operation(
        summary = "본인의 대여 기록 조회",
        description = "대여 상태 별로 본인의 대여 기록을 조회하는 API"
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode = "200",
                description = "대여 목록 조회 완료"
            )
        ]
    )
    fun getMemberRentalHistory(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestParam rentalStatus: RentalStatus?)
    : ResponseEntity<List<RentalHistoryDetail>>
}