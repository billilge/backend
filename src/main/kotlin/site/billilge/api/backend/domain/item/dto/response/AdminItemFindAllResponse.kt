package site.billilge.api.backend.domain.item.dto.response

import site.billilge.api.backend.global.dto.PageableResponse

data class AdminItemFindAllResponse(
    val items: List<AdminItemDetail>,
    override val totalPage: Int
): PageableResponse