package site.billilge.api.backend.domain.rental.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.ReturnRequiredItemFindAllResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.facade.RentalFacade
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/rentals")
class RentalController(
    private val rentalFacade: RentalFacade,
) : RentalApi {

    @PostMapping
    override fun createRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody rentalHistoryRequest: RentalHistoryRequest
    ): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        rentalFacade.createRental(memberId, rentalHistoryRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @OnlyAdmin
    @PostMapping("/dev")
    override fun createDevRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody rentalHistoryRequest: RentalHistoryRequest
    ): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        rentalFacade.createRental(memberId, rentalHistoryRequest, true)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    override fun getMemberRentalHistory(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestParam(required =  false) rentalStatus: RentalStatus?
    ) : ResponseEntity<RentalHistoryFindAllResponse> {
        val memberId = userAuthInfo.memberId
        return ResponseEntity.ok(rentalFacade.getMemberRentalHistory(memberId, rentalStatus))
    }

    @PatchMapping("/{rentalHistoryId}")
    override fun cancelRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        rentalFacade.cancelRental(memberId, rentalHistoryId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PatchMapping("/return/{rentalHistoryId}")
    override fun returnRental(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable rentalHistoryId: Long): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        rentalFacade.returnRental(memberId, rentalHistoryId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping("/return-required")
    override fun getReturnRequiredHistory(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<ReturnRequiredItemFindAllResponse> {
        val memberId = userAuthInfo.memberId
        return ResponseEntity.ok(rentalFacade.getReturnRequiredItems(memberId))
    }
}