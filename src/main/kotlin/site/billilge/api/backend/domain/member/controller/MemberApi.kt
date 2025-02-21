package site.billilge.api.backend.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody
import site.billilge.api.backend.domain.member.dto.request.MemberFCMTokenRequest
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Tag(name = "Member", description = "회원 API")
interface MemberApi {
    @Operation(
        summary = "FCM 토큰 전송",
        description = "서버 측으로 회원 기기의 FCM 토큰을 전송하는 API"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "FCM 토큰 전송 성공"
            )
        ]
    )
    fun setFCMToken(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody request: MemberFCMTokenRequest
    ): ResponseEntity<Void>
}