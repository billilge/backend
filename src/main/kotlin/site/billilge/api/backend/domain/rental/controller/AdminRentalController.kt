package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.rental.dto.request.AdminRentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.request.RentalStatusUpdateRequest
import site.billilge.api.backend.domain.rental.dto.response.AdminRentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.DashboardResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.service.RentalService
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/admin/rentals")
@OnlyAdmin
class AdminRentalController(
    private val rentalService: RentalService
) : AdminRentalApi {
    @GetMapping("/dashboard")
    override fun getAllDashboardApplications(
        @RequestParam(required = false) rentalStatus: RentalStatus?,
    ): ResponseEntity<DashboardResponse> {
        return ResponseEntity.ok(rentalService.getAllDashboardApplications(rentalStatus))
    }

    @GetMapping
    override fun getAllRentalHistories(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminRentalHistoryFindAllResponse> {
        return ResponseEntity.ok(rentalService.getAllRentalHistories(pageableCondition, searchCondition))
    }

    @PatchMapping("/{rentalHistoryId}")
    override fun updateRentalStatus(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long,
        @RequestBody request: RentalStatusUpdateRequest
    ): ResponseEntity<Void> {
        rentalService.updateRentalStatus(userAuthInfo.memberId, rentalHistoryId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    override fun addRentalHistory(
        @RequestBody request: AdminRentalHistoryRequest
    ): ResponseEntity<Void> {
        rentalService.createRentalByAdmin(request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{rentalHistoryId}")
    override fun deleteRentalHistory(@PathVariable rentalHistoryId: Long): ResponseEntity<Void> {
        rentalService.deleteRentalHistory(rentalHistoryId)
        return ResponseEntity.ok().build()
    }
}