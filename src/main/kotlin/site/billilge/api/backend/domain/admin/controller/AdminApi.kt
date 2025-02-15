package site.billilge.api.backend.domain.admin.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.admin.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.global.exception.ErrorResponse

@Tag(name = "Admin", description = "관리자 API")
interface AdminApi {

    @Operation(
        summary = "대여 물품 목록 조회",
        description = "대여 물품 목록을 조회하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "대여 물품 목록 조회 성공"
            )
        ]
    )
    fun getAllItems(): ResponseEntity<ItemFindAllResponse>

    @Operation(
        summary = "대여 물품 등록",
        description = "대여 물품을 등록하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "대여 물품 등록 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "중복된 물품 이름입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun addItem(@RequestPart image: MultipartFile, @RequestPart itemRequest: ItemRequest): ResponseEntity<Void>

    @Operation(
        summary = "대여 물품 수정",
        description = "대여 물품 정보를 수정하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "대여 물품 수정 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "물품 정보가 존재하지 않습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun updateItem(
        @PathVariable itemId: Long,
        @RequestPart image: MultipartFile?,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void>

    @Operation(
        summary = "대여 물품 삭제",
        description = "대여 물품을 삭제하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "대여 물품 삭제 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "물품 정보가 존재하지 않습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun deleteItem(
        @PathVariable itemId: Long,
    ): ResponseEntity<Void>

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

    @Operation(
        summary = "회원 권한 변경",
        description = "회원의 권한(사용자/관리자)을 변경하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "회원이 사용자라면 관리자로, 관리자라면 사용자로 권한 변경 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "회원을 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun updateMemberRole(@PathVariable memberId: Long): ResponseEntity<Void>

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
        @RequestParam(required = false, defaultValue = "0") pageNo: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int,
        @RequestParam(required = false, defaultValue = "enrollmentYear") criteria: String
    ): ResponseEntity<PayerFindAllResponse>
}
