package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.rental.dto.response.DashboardResponse
import site.billilge.api.backend.domain.rental.service.RentalService
import site.billilge.api.backend.global.annotation.OnlyAdmin

@RestController
@RequestMapping("/admin/rentals")
@OnlyAdmin
class AdminRentalController(
    private val rentalService: RentalService
) {
    @GetMapping("/dashboard")
    fun getAllDashboardApplications(): ResponseEntity<DashboardResponse> {
        return ResponseEntity.ok(rentalService.getAllDashboardApplications())
    }

}