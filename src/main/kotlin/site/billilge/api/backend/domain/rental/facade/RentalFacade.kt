package site.billilge.api.backend.domain.rental.facade

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryDetail
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryFindAllResponse
import site.billilge.api.backend.domain.rental.dto.response.ReturnRequiredItemDetail
import site.billilge.api.backend.domain.rental.dto.response.ReturnRequiredItemFindAllResponse
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.service.RentalService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Component
class RentalFacade(
    private val rentalService: RentalService,
    private val memberService: MemberService,
    private val itemService: ItemService,
) {
    @Transactional
    fun createRental(memberId: Long?, rentalHistoryRequest: RentalHistoryRequest, isDevMode: Boolean = false) {
        val rentAt = resolveKoreanRentAt(rentalHistoryRequest.rentalTime.hour, rentalHistoryRequest.rentalTime.minute)
        val member = memberService.findById(memberId!!)
        val item = itemService.getItemById(rentalHistoryRequest.itemId)
        rentalService.createRental(
            member,
            item,
            rentalHistoryRequest.count,
            rentAt,
            rentalHistoryRequest.ignoreDuplicate,
            isDevMode
        )
    }

    fun getMemberRentalHistory(memberId: Long?, rentalStatus: RentalStatus?): RentalHistoryFindAllResponse {
        val rentalHistories = rentalService.getMemberRentalHistory(memberId, rentalStatus)
        return RentalHistoryFindAllResponse(
            rentalHistories.map { RentalHistoryDetail.from(it) }
        )
    }

    fun cancelRental(memberId: Long?, rentalHistoryId: Long) {
        rentalService.cancelRental(memberId, rentalHistoryId)
    }

    fun returnRental(memberId: Long?, rentalHistoryId: Long) {
        rentalService.returnRental(memberId, rentalHistoryId)
    }

    fun getReturnRequiredItems(memberId: Long?): ReturnRequiredItemFindAllResponse {
        val rentalHistories = rentalService.getReturnRequiredItems(memberId)
        return ReturnRequiredItemFindAllResponse(
            rentalHistories.map { ReturnRequiredItemDetail.from(it) }
        )
    }

    private fun resolveKoreanRentAt(hour: Int, minute: Int): LocalDateTime {
        val koreanZone = ZoneId.of("Asia/Seoul")
        val today = LocalDate.now(koreanZone)
        return LocalDateTime.of(today, LocalTime.of(hour, minute))
    }
}
