package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.rental.dto.request.AdminRentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.request.RentalStatusUpdateRequest
import site.billilge.api.backend.domain.rental.dto.response.AdminRentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.DashboardResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.facade.AdminRentalFacade
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/admin/rentals")
@OnlyAdmin(roles = [Role.ADMIN, Role.GA, Role.WORKER])
class AdminRentalController(
    private val adminRentalFacade: AdminRentalFacade
) : AdminRentalApi {
    @GetMapping("/dashboard")
    override fun getAllDashboardApplications(
        @RequestParam(required = false) rentalStatus: RentalStatus?,
    ): ResponseEntity<DashboardResponse> {
        return ResponseEntity.ok(adminRentalFacade.getAllDashboardApplications(rentalStatus))
    }

    @GetMapping
    override fun getAllRentalHistories(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminRentalHistoryFindAllResponse> {
        return ResponseEntity.ok(adminRentalFacade.getAllRentalHistories(pageableCondition, searchCondition))
    }

    @PatchMapping("/{rentalHistoryId}")
    override fun updateRentalStatus(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long,
        @RequestBody request: RentalStatusUpdateRequest
    ): ResponseEntity<Void> {
        adminRentalFacade.updateRentalStatus(userAuthInfo.memberId, rentalHistoryId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    override fun addRentalHistory(
        @RequestBody request: AdminRentalHistoryRequest
    ): ResponseEntity<Void> {
        adminRentalFacade.createRentalByAdmin(request)
        return ResponseEntity.ok().build()
    }

    @OnlyAdmin
    @DeleteMapping("/{rentalHistoryId}")
    override fun deleteRentalHistory(@PathVariable rentalHistoryId: Long): ResponseEntity<Void> {
        adminRentalFacade.deleteRentalHistory(rentalHistoryId)
        return ResponseEntity.ok().build()
    }
}