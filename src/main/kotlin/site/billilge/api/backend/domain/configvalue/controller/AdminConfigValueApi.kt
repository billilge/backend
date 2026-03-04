package site.billilge.api.backend.domain.configvalue.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.configvalue.dto.request.ChangeAdminPasswordRequest
import site.billilge.api.backend.domain.configvalue.dto.request.ConfigValueBulkUpdateRequest
import site.billilge.api.backend.domain.configvalue.dto.request.ConfigValueUpdateRequest
import site.billilge.api.backend.domain.configvalue.dto.response.ConfigValueDetail
import site.billilge.api.backend.domain.configvalue.dto.response.ConfigValueFindAllResponse

@Tag(name = "(Admin) ConfigValue", description = "관리자용 설정값 API")
interface AdminConfigValueApi {
    @Operation(
        summary = "설정값 조회",
        description = "키로 설정값을 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "설정값 조회 성공"),
            ApiResponse(responseCode = "404", description = "설정값을 찾을 수 없습니다.")
        ]
    )
    fun getByKey(@RequestParam key: String): ResponseEntity<ConfigValueDetail>

    @Operation(
        summary = "설정값 일괄 조회",
        description = "여러 키로 설정값을 일괄 조회하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "설정값 일괄 조회 성공")
        ]
    )
    fun getAllByKeys(@RequestParam keys: List<String>): ResponseEntity<ConfigValueFindAllResponse>

    @Operation(
        summary = "설정값 수정",
        description = "설정값을 수정하는 API (존재하지 않으면 생성)"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "설정값 수정 성공")
        ]
    )
    fun update(@RequestBody request: ConfigValueUpdateRequest): ResponseEntity<Void>

    @Operation(
        summary = "설정값 일괄 수정",
        description = "여러 설정값을 일괄 수정하는 API (존재하지 않으면 생성)"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "설정값 일괄 수정 성공")
        ]
    )
    fun updateAll(@RequestBody request: ConfigValueBulkUpdateRequest): ResponseEntity<Void>

    @Operation(
        summary = "관리자 비밀번호 변경",
        description = "현재 비밀번호를 검증 후 새 비밀번호로 변경하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            ApiResponse(responseCode = "400", description = "현재 비밀번호가 일치하지 않습니다.")
        ]
    )
    fun changeAdminPassword(@RequestBody request: ChangeAdminPasswordRequest): ResponseEntity<Void>
}
