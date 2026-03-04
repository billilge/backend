package site.billilge.api.backend.domain.rental.facade

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.rental.dto.request.AdminRentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.request.RentalStatusUpdateRequest
import site.billilge.api.backend.domain.rental.dto.response.AdminRentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.DashboardResponse
import site.billilge.api.backend.domain.rental.dto.response.RentalStatusWorkerLogFindAllResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.service.RentalService
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Component
class AdminRentalFacade(
    private val rentalService: RentalService,
    private val memberService: MemberService,
    private val itemService: ItemService,
) {
    fun getAllDashboardApplications(rentalStatus: RentalStatus?): DashboardResponse {
        val rentalHistories = rentalService.getAllDashboardApplications(rentalStatus)
        return DashboardResponse(
            rentalHistories.map { DashboardResponse.RentalApplicationDetail.from(it) }
        )
    }

    fun getAllRentalHistories(pageableCondition: PageableCondition, searchCondition: SearchCondition): AdminRentalHistoryFindAllResponse {
        val results = rentalService.getAllRentalHistories(pageableCondition, searchCondition)
        val adminRentalHistoryDetails = results
            .map { AdminRentalHistoryFindAllResponse.AdminRentalHistoryDetail.from(it) }
            .toList()
        return AdminRentalHistoryFindAllResponse(adminRentalHistoryDetails, results.totalPages)
    }

    @Transactional
    fun updateRentalStatus(workerId: Long?, rentalHistoryId: Long, request: RentalStatusUpdateRequest) {
        val worker = memberService.findById(workerId!!)
        rentalService.updateRentalStatus(worker, rentalHistoryId, request.rentalStatus)
    }

    @Transactional
    fun createRentalByAdmin(request: AdminRentalHistoryRequest) {
        val rentAt = resolveKoreanRentAt(request.rentalTime.hour, request.rentalTime.minute)
        val worker = memberService.findById(request.workerId)
        val member = memberService.findById(request.memberId)
        val item = itemService.getItemById(request.itemId)
        rentalService.createRentalByAdmin(
            worker,
            member,
            item,
            request.count,
            rentAt
        )
    }

    fun updateItemCode(rentalHistoryId: Long, itemCode: String) {
        rentalService.updateItemCode(rentalHistoryId, itemCode)
    }

    fun getWorkerLogs(rentalHistoryId: Long): RentalStatusWorkerLogFindAllResponse {
        val logs = rentalService.getWorkerLogsByRentalHistoryId(rentalHistoryId)
        return RentalStatusWorkerLogFindAllResponse(
            logs.map { RentalStatusWorkerLogFindAllResponse.RentalStatusWorkerLogDetail.from(it) }
        )
    }

    fun deleteRentalHistory(rentalHistoryId: Long) {
        rentalService.deleteRentalHistory(rentalHistoryId)
    }

    private fun resolveKoreanRentAt(hour: Int, minute: Int): LocalDateTime {
        val koreanZone = ZoneId.of("Asia/Seoul")
        val today = LocalDate.now(koreanZone)
        return LocalDateTime.of(today, LocalTime.of(hour, minute))
    }
}
