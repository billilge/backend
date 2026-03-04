package site.billilge.api.backend.domain.payer.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.payer.dto.request.PayerDeleteRequest
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.domain.payer.dto.response.PayerSummary
import site.billilge.api.backend.domain.payer.service.PayerService
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import java.io.ByteArrayInputStream

@Component
class AdminPayerFacade(
    private val payerService: PayerService,
) {
    fun getAllPayers(pageableCondition: PageableCondition, searchCondition: SearchCondition): PayerFindAllResponse {
        val payers = payerService.getAllPayers(pageableCondition, searchCondition)
        val payerSummaries = payers.map { PayerSummary.from(it) }.toList()
        return PayerFindAllResponse(payerSummaries, payers.totalPages)
    }

    fun addPayers(request: PayerRequest) {
        val payers = request.payers.map { it.name to it.studentId }
        payerService.addPayers(payers)
    }

    fun deletePayers(request: PayerDeleteRequest) {
        payerService.deletePayers(request.payerIds)
    }

    fun createPayerExcel(): ByteArrayInputStream {
        return payerService.createPayerExcel()
    }
}
