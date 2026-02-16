package site.billilge.api.backend.domain.item.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.AdminItemFindAllResponse
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.facade.AdminItemFacade
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@RestController
@RequestMapping("/admin/items")
@OnlyAdmin(roles = [Role.ADMIN, Role.GA, Role.WORKER])
class AdminItemController(
    private val adminItemFacade: AdminItemFacade
) : AdminItemApi {
    @GetMapping
    override fun getAllAdminItems(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminItemFindAllResponse> {
        return ResponseEntity.ok(
            adminItemFacade.getAllAdminItems(
                pageableCondition,
                searchCondition
            )
        )
    }

    @OnlyAdmin
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun addItem(
        @RequestPart image: MultipartFile,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void> {
        adminItemFacade.addItem(image, itemRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @OnlyAdmin
    @PutMapping("/{itemId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun updateItem(
        @PathVariable itemId: Long,
        @RequestPart image: MultipartFile?,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void> {
        adminItemFacade.updateItem(image, itemId, itemRequest)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{itemId}")
    override fun getItemById(
        @PathVariable itemId: Long
    ): ResponseEntity<ItemDetail> {
        return ResponseEntity.ok(adminItemFacade.getItemById(itemId))
    }

    @OnlyAdmin
    @DeleteMapping("/{itemId}")
    override fun deleteItem(@PathVariable itemId: Long): ResponseEntity<Void> {
        adminItemFacade.deleteItem(itemId)
        return ResponseEntity.noContent().build()
    }
}