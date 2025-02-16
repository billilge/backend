package site.billilge.api.backend.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse

@Tag(name = "(Admin) Member", description = "관리자용 관리자 API")
interface AdminMemberApi {
    @Operation(
        summary = "관리자 목록 조회",
        description = "관리자 목록을 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "관리자 목록 조회 성공"
            )
        ]
    )
    fun getAdminList(): ResponseEntity<AdminFindAllResponse>
}