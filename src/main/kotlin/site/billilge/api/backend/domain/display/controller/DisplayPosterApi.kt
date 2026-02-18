package site.billilge.api.backend.domain.display.controller

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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.display.dto.request.DisplayPosterRequest
import site.billilge.api.backend.domain.display.dto.response.DisplayPosterFindAllResponse
import site.billilge.api.backend.global.exception.ErrorResponse

@Tag(name = "(Admin) Display Poster", description = "관리자용 전광판 포스터 API")
interface DisplayPosterApi {

    @Operation(
        summary = "포스터 목록 조회",
        description = "전체 포스터 목록을 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "포스터 목록 조회 성공"
            )
        ]
    )
    fun getAllPosters(): ResponseEntity<DisplayPosterFindAllResponse>

    @Operation(
        summary = "포스터 추가",
        description = "포스터를 추가하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "포스터 추가 성공"
            )
        ]
    )
    @RequestBody(
        content = [Content(
            encoding = [
                Encoding(name = "posterRequest", contentType = MediaType.APPLICATION_JSON_VALUE)
            ]
        )]
    )
    fun addPoster(
        @RequestPart image: MultipartFile,
        @RequestPart posterRequest: DisplayPosterRequest
    ): ResponseEntity<Void>

    @Operation(
        summary = "포스터 수정",
        description = "포스터 정보를 수정하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "포스터 수정 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "포스터를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @RequestBody(
        content = [Content(
            encoding = [
                Encoding(name = "posterRequest", contentType = MediaType.APPLICATION_JSON_VALUE)
            ]
        )]
    )
    fun updatePoster(
        @PathVariable posterId: Long,
        @RequestPart(required = false) image: MultipartFile?,
        @RequestPart posterRequest: DisplayPosterRequest
    ): ResponseEntity<Void>

    @Operation(
        summary = "포스터 삭제",
        description = "포스터를 삭제하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "포스터 삭제 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "포스터를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun deletePoster(
        @PathVariable posterId: Long
    ): ResponseEntity<Void>

    @Operation(
        summary = "포스터 활성화",
        description = "포스터를 활성화하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "포스터 활성화 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "포스터를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun activatePoster(
        @PathVariable posterId: Long
    ): ResponseEntity<Void>

    @Operation(
        summary = "포스터 비활성화",
        description = "포스터를 비활성화하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "포스터 비활성화 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "포스터를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun deactivatePoster(
        @PathVariable posterId: Long
    ): ResponseEntity<Void>
}
