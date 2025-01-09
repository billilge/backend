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
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
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
}
