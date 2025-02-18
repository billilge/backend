package site.billilge.api.backend.domain.member.dto.response

import site.billilge.api.backend.global.dto.PageableResponse

data class MemberFindAllResponse(
    val members: List<MemberDetail>,
    override val totalPage: Int
): PageableResponse
