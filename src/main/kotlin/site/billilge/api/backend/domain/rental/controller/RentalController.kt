package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.service.RentalService
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/rentals")
class RentalController(
    private val rentalService: RentalService,
) : RentalApi {

    @PostMapping()
    override fun createRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody rentalHistoryRequest: RentalHistoryRequest
    ): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        rentalService.createRental(memberId, rentalHistoryRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    override fun getMemberRentalHistory(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestParam(required =  false) rentalStatus: RentalStatus?
    ) : ResponseEntity<RentalHistoryFindAllResponse> {
        val memberId = userAuthInfo.memberId
        return ResponseEntity.ok(rentalService.getMemberRentalHistory(memberId, rentalStatus))
    }
}