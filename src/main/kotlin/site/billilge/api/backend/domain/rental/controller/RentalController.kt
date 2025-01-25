package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.rental.dto.request.RentalRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalStatusResponse
import site.billilge.api.backend.domain.rental.service.RentalService
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/rentals")
class RentalController(
    private val rentalService: RentalService,
) : RentalApi {

    @PostMapping("")
    override fun createRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody rentalRequest: RentalRequest
    ): ResponseEntity<Void> {
        val memberId = userAuthInfo.getMemberId()
        rentalService.createRental(memberId, rentalRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/{itemId}")
    override fun getRentalStatus(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable itemId: Long
    ): ResponseEntity<RentalStatusResponse> {
        val memberId = userAuthInfo.getMemberId()
        return ResponseEntity.ok(rentalService.getRentalStatus(memberId, itemId))
    }
}