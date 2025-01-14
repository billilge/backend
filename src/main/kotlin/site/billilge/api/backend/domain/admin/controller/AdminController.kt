package site.billilge.api.backend.domain.admin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.admin.service.AdminService
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.global.annotation.OnlyAdmin

@RestController
@RequestMapping("/admin")
@OnlyAdmin
class AdminController(
    private val adminService: AdminService,
) : AdminApi {

    @GetMapping("/items")
    override fun getAllItems(): ResponseEntity<ItemFindAllResponse> {
        return ResponseEntity.ok(adminService.getAllItems())
    }

    @PostMapping("/items", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun addItem(
        @RequestPart image: MultipartFile,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void> {
        adminService.addItem(image, itemRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/items/{itemId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun updateItem(
        @PathVariable itemId: Long,
        @RequestPart image: MultipartFile?,
        @RequestPart itemRequest: ItemRequest
    ): ResponseEntity<Void> {
        adminService.updateItem(image, itemId, itemRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/items/{itemId}")
    override fun deleteItem(@PathVariable itemId: Long): ResponseEntity<Void> {
        adminService.deleteItem(itemId)
        return ResponseEntity.noContent().build()
    }
}