package site.billilge.api.backend.domain.rental.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryFindAllResponse
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
        @RequestBody rentalHistoryRequest: RentalHistoryRequest): ResponseEntity<Void>

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
        @RequestParam(required = false) rentalStatus: RentalStatus?)
    : ResponseEntity<RentalHistoryFindAllResponse>


    @Operation(
        summary = "대여 취소",
        description = "대여 신청을 취소하는 API입니다. 신청한 사용자는 대여 신청을 취소할 수 있습니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "대여 취소 완료"),
        ]
    )
    fun cancelRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long
    ): ResponseEntity<Void>

    @Operation(
        summary = "반납 신청",
        description = "대여한 물품의 반납을 신청하는 API입니다. 반납 요청을 통해 대여 상태가 변경됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "반납 신청 완료"),
        ]
    )
    fun returnRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long
    ): ResponseEntity<Void>
}