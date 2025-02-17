package site.billilge.api.backend.domain.member.dto.response

import site.billilge.api.backend.global.dto.PageableResponse

data class AdminFindAllResponse(
    val admins: List<AdminMemberDetail>,
    override val totalPage: Int
) : PageableResponse
