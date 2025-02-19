package site.billilge.api.backend.domain.rental.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "반납이 필요한 물품들의 목록 응답")
data class ReturnRequiredItemFindAllResponse(
    @field:Schema(description = "반납이 필요한 물품 상세 정보 목록")
    val items: List<ReturnRequiredItemDetail>
)