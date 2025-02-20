package site.billilge.api.backend.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class MemberFCMTokenRequest(
    @field:Schema(description = "회원의 디바이스 FCM 토큰")
    val token: String
)
