package site.billilge.api.backend.domain.item.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Encoding
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.AdminItemFindAllResponse
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.exception.ErrorResponse

@Tag(name = "(Admin) Item", description = "관리자용 물품 API")
interface AdminItemApi {
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
    fun getAllAdminItems(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminItemFindAllResponse>

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
    @RequestBody(
        content = [Content(
            encoding = [
                Encoding(name = "itemRequest", contentType = MediaType.APPLICATION_JSON_VALUE)]
        )]
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
    @RequestBody(
        content = [Content(
            encoding = [
                Encoding(name = "itemRequest", contentType = MediaType.APPLICATION_JSON_VALUE)]
        )]
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
        summary = "물품 상세 조회",
        description = "ID로 물품을 상세 조회하는 관리자용 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "대여 물품 조회 성공"
            )
        ]
    )
    fun getItemById(
        @PathVariable itemId: Long,
    ): ResponseEntity<ItemDetail>
}