package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.global.dto.PageableResponse

@Schema
data class MemberFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = MemberDetail::class))
    val members: List<MemberDetail>,
    @field:Schema(description = "총 페이지 수", example = "1")
    override val totalPage: Int
): PageableResponse
