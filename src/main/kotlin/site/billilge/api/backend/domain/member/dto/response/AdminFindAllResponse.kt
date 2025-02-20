package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.global.dto.PageableResponse

@Schema
data class AdminFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = AdminMemberDetail::class))
    val admins: List<AdminMemberDetail>,
    @field:Schema(description = "총 페이지 수", example = "1")
    override val totalPage: Int
) : PageableResponse
