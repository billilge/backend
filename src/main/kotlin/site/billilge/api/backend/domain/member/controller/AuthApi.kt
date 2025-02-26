package site.billilge.api.backend.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.member.dto.request.AdminLoginRequest
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.AdminLoginResponse
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.global.exception.ErrorResponse

@Tag(name = "Auth", description = "로그인 인증 API")
interface AuthApi {
    @Operation(
        summary = "(구글 신규 가입 시) 회원가입",
        description = "구글 로그인으로 신규 가입 시 학번, 이름을 받기 위한 회원가입 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "회원가입 성공"
            )
        ]
    )
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse>

    @Operation(
        summary = "(관리자 데스크탑) 로그인",
        description = "관리자 데스크탑 페이지에 접속하기 위한 로그인 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "로그인 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "회원을 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "권한이 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "비밀번호가 일치하지 않습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun loginAdmin(@RequestBody request: AdminLoginRequest): ResponseEntity<AdminLoginResponse>

    @Operation(hidden = true)
    fun googleLoginCallback(@RequestParam code: String?): ResponseEntity<Void>
}