package site.billilge.api.backend.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.global.dto.PageableCondition

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
    fun getAdminList(
        @ModelAttribute pageableCondition: PageableCondition,
//        @RequestParam(required = false, defaultValue = "0") pageNo: Int,
//        @RequestParam(required = false, defaultValue = "10") size: Int
    ): ResponseEntity<AdminFindAllResponse>
}