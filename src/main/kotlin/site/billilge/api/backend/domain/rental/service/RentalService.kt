package site.billilge.api.backend.domain.rental.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.response.*
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.exception.RentalErrorCode
import site.billilge.api.backend.domain.rental.repository.RentalRepository
import site.billilge.api.backend.global.exception.ApiException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Service
@Transactional(readOnly = true)
class RentalService(
    private val memberRepository: MemberRepository,
    private val rentalRepository: RentalRepository,
    private val itemRepository: ItemRepository,
) {
    @Transactional
    fun createRental(memberId: Long?, rentalHistoryRequest: RentalHistoryRequest) {
        val item = itemRepository.findById(rentalHistoryRequest.itemId)
            .orElseThrow { ApiException(RentalErrorCode.ITEM_NOT_FOUND) }

        if (!rentalHistoryRequest.ignoreDuplicate) {
            val rentalHistory = rentalRepository.findByItemIdAndMemberIdAndRentalStatus(
                rentalHistoryRequest.itemId,
                memberId!!,
                RentalStatus.RENTAL
            )

            if (rentalHistory.isPresent)
                throw ApiException(RentalErrorCode.RENTAL_ITEM_DUPLICATED)
        }

        if (rentalHistoryRequest.count > item.count)
            throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)

        val rentUser = memberRepository.findById(memberId!!)
            .orElseThrow { ApiException(RentalErrorCode.MEMBER_NOT_FOUND) }

        val koreanZone = ZoneId.of("Asia/Seoul")
        val today = LocalDate.now(koreanZone)
        val requestedRentalDateTime = LocalDateTime.of(
            today,
            LocalTime.of(rentalHistoryRequest.rentalTime.hour, rentalHistoryRequest.rentalTime.minute)
        )

        val currentKoreanTime = LocalDateTime.now(koreanZone)
        if (requestedRentalDateTime.isBefore(currentKoreanTime)) {
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_PAST)
        }

        val rentalHour = requestedRentalDateTime.hour
        if (rentalHour < 10 || rentalHour > 17) {
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_PAST)
        }

        val rentAt = requestedRentalDateTime.atZone(koreanZone).toLocalDateTime()

        val newRental = RentalHistory(
            member = rentUser,
            item = item,
            rentalStatus = RentalStatus.PENDING,
            rentAt = rentAt
        )

        rentalRepository.save(newRental)
    }

    fun getMemberRentalHistory(memberId: Long?, rentalStatus: RentalStatus?): RentalHistoryFindAllResponse {
        val rentalHistories = if (rentalStatus == null) {
            rentalRepository.findByMemberId(memberId!!)
        } else {
            rentalRepository.findByMemberIdAndRentalStatus(memberId!!, rentalStatus)
        }
        return RentalHistoryFindAllResponse(
            rentalHistories
                .map { rentalHistory -> RentalHistoryDetail.from(rentalHistory) })
    }

    @Transactional
    fun cancelRental(memberId: Long?, rentalHistoryId: Long) {
        val rentalHistory = rentalRepository.findById(rentalHistoryId)
            .orElseThrow { ApiException(RentalErrorCode.RENTAL_NOT_FOUND) }

        rentalHistory.updateStatus(RentalStatus.CANCEL)

        rentalRepository.save(rentalHistory)
    }

    @Transactional
    fun returnRental(memberId: Long?, rentalHistoryId: Long) {
        val rentalHistory = rentalRepository.findById(rentalHistoryId)
            .orElseThrow { ApiException(RentalErrorCode.RENTAL_NOT_FOUND) }

        rentalHistory.updateStatus(RentalStatus.RETURN_PENDING)

        rentalRepository.save(rentalHistory)
    }

    fun getReturnRequiredItems(memberId: Long?): ReturnRequiredItemFindAllResponse {
        val returnRequiredItems = rentalRepository.findByMemberIdAndRentalStatusIn(memberId!!, RETURN_REQUIRED_STATUS);

        return ReturnRequiredItemFindAllResponse(
            returnRequiredItems.map { ReturnRequiredItemDetail.from(it) })
    }

    fun getAllDashboardApplications(): DashboardResponse {
        val rentalApplicationDetails = rentalRepository.findAllByRentalStatusIn(DASHBOARD_STATUS)
            .map { DashboardResponse.RentalApplicationDetail.from(it) }

        return DashboardResponse(rentalApplicationDetails)
    }

    companion object {
        private val DASHBOARD_STATUS = listOf(RentalStatus.PENDING, RentalStatus.RETURN_PENDING)
        private val RETURN_REQUIRED_STATUS = listOf(RentalStatus.RENTAL, RentalStatus.RETURN_PENDING, RentalStatus.RETURN_CONFIRMED)
    }
}