package site.billilge.api.backend.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse

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

    @Operation(hidden = true)
    fun googleLoginCallback(@RequestParam code: String?): ResponseEntity<Void>
}