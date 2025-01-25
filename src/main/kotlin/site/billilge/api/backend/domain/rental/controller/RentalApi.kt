package site.billilge.api.backend.domain.rental.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import site.billilge.api.backend.domain.rental.dto.request.RentalRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalStatusResponse
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
        summary = "특정 물품 대여 여부 확인",
        description = "물품을 대여하기 전 중복된 물품을 대여하고 있는지 확인하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode =  "200",
                description = "중복 대여 여부 조회 완료"
            )
        ]
    )
    fun getRentalStatus(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable itemId: Long
    ): ResponseEntity<RentalStatusResponse>
}