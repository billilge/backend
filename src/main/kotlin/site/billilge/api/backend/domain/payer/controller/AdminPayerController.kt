package site.billilge.api.backend.domain.payer.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.payer.dto.request.PayerDeleteRequest
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.domain.payer.service.PayerService
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@RestController
@RequestMapping("/admin/members/payers")
@OnlyAdmin
class AdminPayerController(
    private val payerService: PayerService
) : AdminPayerApi {
    @GetMapping
    override fun getAllPayers(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition,
    ): ResponseEntity<PayerFindAllResponse> {
        return ResponseEntity.ok(payerService.getAllPayers(pageableCondition, searchCondition))
    }

    @PostMapping
    override fun addPayers(@RequestBody request: PayerRequest): ResponseEntity<Void> {
        payerService.addPayers(request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    override fun deletePayers(@RequestBody request: PayerDeleteRequest): ResponseEntity<Void> {
        payerService.deletePayers(request)
        return ResponseEntity.noContent().build()
    }
}