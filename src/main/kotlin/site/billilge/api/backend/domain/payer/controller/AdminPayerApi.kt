package site.billilge.api.backend.domain.payer.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.payer.dto.request.PayerDeleteRequest
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@Tag(name = "(Admin) Payer", description = "관리자용 학생회비 납부자 API")
interface AdminPayerApi {
    @Operation(
        summary = "학생회비 납부자 추가",
        description = "학생회비 납부자 데이터를 추가하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "학생회비 납부자 데이터 추가 성공"
            )
        ]
    )
    fun addPayers(@RequestBody request: PayerRequest): ResponseEntity<Void>

    @Operation(
        summary = "학생회비 납부자 목록 조회",
        description = "학생회비 납부자 목록을 전체 혹은 페이지별로 조회하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "학생회비 납부자 목록 조회 성공"
            )
        ]
    )
    fun getAllPayers(
        pageableCondition: PageableCondition,
        searchCondition: SearchCondition
//        @RequestParam(required = false, defaultValue = "0") pageNo: Int,
//        @RequestParam(required = false, defaultValue = "10") size: Int,
//        @RequestParam(required = false, defaultValue = "enrollmentYear") criteria: String
    ): ResponseEntity<PayerFindAllResponse>

    @Operation(
        summary = "학생회비 납부자 삭제",
        description = "학생회비 납부자를 삭제하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "학생회비 납부자 삭제 성공"
            )
        ]
    )
    fun deletePayers(@RequestBody request: PayerDeleteRequest): ResponseEntity<Void>

    @Operation(
        summary = "학생회비 납부자 Excel 파일 다운로드",
        description = "학생회비 납부자 데이터를 엑셀 파일로 다운받을 수 있는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "파일 다운로드 성공",
                content = [Content(
                    mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = Schema(type = "string", format = "binary")
                )]
            )
        ]
    )
    fun createPayerExcel(): ResponseEntity<InputStreamResource>
}