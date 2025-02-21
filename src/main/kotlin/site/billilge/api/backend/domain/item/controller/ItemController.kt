package site.billilge.api.backend.domain.item.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.global.dto.SearchCondition

@RestController
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService
) : ItemApi {
    @GetMapping
    override fun getItems(
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<ItemFindAllResponse> {
        val response = itemService.searchItems(searchCondition)
        return ResponseEntity.ok(response)
    }
}