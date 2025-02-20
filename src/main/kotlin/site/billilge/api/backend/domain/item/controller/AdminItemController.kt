package site.billilge.api.backend.domain.item.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.AdminItemFindAllResponse
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@RestController
@RequestMapping("/admin/items")
@OnlyAdmin
class AdminItemController(
    private val itemService: ItemService
) : AdminItemApi {
    @GetMapping
    override fun getAllAdminItems(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminItemFindAllResponse> {
        return ResponseEntity.ok(
            itemService.getAllAdminItems(
                pageableCondition,
                searchCondition
            )
        )
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun addItem(
        @RequestPart image: MultipartFile,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void> {
        itemService.addItem(image, itemRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{itemId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun updateItem(
        @PathVariable itemId: Long,
        @RequestPart image: MultipartFile?,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void> {
        itemService.updateItem(image, itemId, itemRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{itemId}")
    override fun deleteItem(@PathVariable itemId: Long): ResponseEntity<Void> {
        itemService.deleteItem(itemId)
        return ResponseEntity.noContent().build()
    }
}