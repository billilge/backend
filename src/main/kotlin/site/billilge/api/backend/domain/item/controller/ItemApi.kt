package site.billilge.api.backend.domain.item.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.global.dto.SearchCondition

@Tag(name = "Item", description = "대여 물품 조회 API")
interface ItemApi {

    @Operation(
        summary = "대여 물품 목록 조회",
        description = "검색어에 따라 물품 이름에 포함된 단어를 기반으로 대여 물품 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "대여 물품 목록 조회 성공"
            )
        ]
    )
    @GetMapping
    fun getItems(
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<ItemFindAllResponse>
}