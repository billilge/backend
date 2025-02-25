package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.rental.dto.request.RentalStatusUpdateRequest
import site.billilge.api.backend.domain.rental.dto.response.AdminRentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.DashboardResponse
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
    override fun getAllDashboardApplications(): ResponseEntity<DashboardResponse> {
        return ResponseEntity.ok(rentalService.getAllDashboardApplications())
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
}