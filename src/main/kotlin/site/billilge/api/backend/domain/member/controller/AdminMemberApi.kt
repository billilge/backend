package site.billilge.api.backend.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import site.billilge.api.backend.domain.member.dto.request.AdminRequest
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.dto.response.MemberFindAllResponse
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@Tag(name = "(Admin) Member", description = "관리자용 관리자 API")
interface AdminMemberApi {
    @Operation(
        summary = "회원 목록 조회",
        description = "회원 목록을 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "회원 목록 조회 성공"
            )
        ]
    )
    fun getAllMembers(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<MemberFindAllResponse>

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
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminFindAllResponse>

    @Operation(
        summary = "관리자 추가",
        description = "회원을 관리자에 추가하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "관리자 추가 성공"
            )
        ]
    )
    fun addAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void>

    @Operation(
        summary = "관리자 삭제",
        description = "관리자 목록에서 회원을 삭제하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "관리자 삭제 성공"
            )
        ]
    )
    fun deleteAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void>
}