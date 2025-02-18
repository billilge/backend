package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class SignUpResponse(
    @field:Schema(description = "액세스 토큰", example = "eySdjeofjaeSfjeoaerrglbvklmsMsldfl")
    val accessToken: String
)
