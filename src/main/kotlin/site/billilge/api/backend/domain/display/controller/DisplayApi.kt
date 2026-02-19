package site.billilge.api.backend.domain.display.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import site.billilge.api.backend.domain.display.dto.response.DisplayResponse

@Tag(name = "Display", description = "디스플레이 조회 API")
interface DisplayApi {

    @Operation(
        summary = "디스플레이 데이터 조회",
        description = "활성 포스터, 일정 캘린더(±3일), 복지물품 현황을 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "디스플레이 데이터 조회 성공"
            )
        ]
    )
    fun getDisplay(): ResponseEntity<DisplayResponse>
}
