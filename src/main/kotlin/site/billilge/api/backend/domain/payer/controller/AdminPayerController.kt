package site.billilge.api.backend.domain.payer.controller

import org.springframework.core.io.InputStreamResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.payer.dto.request.PayerDeleteRequest
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.domain.payer.service.PayerService
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    @GetMapping("/excel")
    override fun createPayerExcel(): ResponseEntity<InputStreamResource> {
        val excel = payerService.createPayerExcel()
        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val headers = HttpHeaders().apply {
            contentDisposition = ContentDisposition.builder("attachment")
                .filename("kmusw_payers_${dateFormatter.format(currentDate)}.xlsx")
                .build()
        }
        val excelMediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(excelMediaType)
            .body(InputStreamResource((excel)))
    }
}