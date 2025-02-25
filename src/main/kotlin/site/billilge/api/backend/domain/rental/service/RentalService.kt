package site.billilge.api.backend.domain.rental.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.service.NotificationService
import site.billilge.api.backend.domain.rental.dto.request.RentalHistoryRequest
import site.billilge.api.backend.domain.rental.dto.request.RentalStatusUpdateRequest
import site.billilge.api.backend.domain.rental.dto.response.*
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.exception.RentalErrorCode
import site.billilge.api.backend.domain.rental.repository.RentalRepository
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
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
    private val notificationService: NotificationService
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

        if (!rentUser.isFeePaid)
            throw ApiException(RentalErrorCode.MEMBER_IS_NOT_PAYER)

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
        val rentalMinute = requestedRentalDateTime.minute
        if (rentalHour < 10 || rentalHour > 17) {
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_OUT_OF_RANGE)
        }

        val rentAt = requestedRentalDateTime.atZone(koreanZone).toLocalDateTime()

        val newRental = RentalHistory(
            member = rentUser,
            item = item,
            rentalStatus = RentalStatus.PENDING,
            rentAt = rentAt
        )

        item.subtractCount(rentalHistoryRequest.count)

        rentalRepository.save(newRental)

        notificationService.sendNotification(
            rentUser,
            NotificationStatus.USER_RENTAL_APPLY,
            listOf(
                item.name
            ),
            true
        )

        notificationService.sendNotificationToAdmin(
            NotificationStatus.ADMIN_RENTAL_APPLY,
            listOf(
                rentUser.name,
                rentUser.studentId,
                "${String.format("%02d", rentalHour)}:${String.format("%02d", rentalMinute)}",
                item.name
            ),
            true
        )
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
        val renter = rentalHistory.member

        rentalHistory.updateStatus(RentalStatus.CANCEL)

        notificationService.sendNotificationToAdmin(
            NotificationStatus.ADMIN_RENTAL_CANCEL,
            listOf(
                renter.name,
                renter.studentId,
                rentalHistory.item.name
            ),
            true
        )
    }

    @Transactional
    fun returnRental(memberId: Long?, rentalHistoryId: Long) {
        val rentalHistory = rentalRepository.findById(rentalHistoryId)
            .orElseThrow { ApiException(RentalErrorCode.RENTAL_NOT_FOUND) }
        val renter = rentalHistory.member

        rentalHistory.updateStatus(RentalStatus.RETURN_PENDING)

        notificationService.sendNotification(
            renter,
            NotificationStatus.USER_RETURN_APPLY,
            listOf(
                rentalHistory.item.name
            ),
            true
        )

        notificationService.sendNotificationToAdmin(
            NotificationStatus.ADMIN_RETURN_APPLY,
            listOf(
                renter.name,
                renter.studentId,
                rentalHistory.item.name
            ),
            true
        )
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

    fun getAllRentalHistories(
        pageableCondition: PageableCondition,
        searchCondition: SearchCondition
    ): AdminRentalHistoryFindAllResponse {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.DESC, pageableCondition.criteria ?: "applicatedAt")
        )
        val results = rentalRepository.findAllByMemberNameContaining(searchCondition.search, pageRequest)
        val adminRentalHistoryDetails = results
            .map { AdminRentalHistoryFindAllResponse.AdminRentalHistoryDetail.from(it) }
            .toList()

        return AdminRentalHistoryFindAllResponse(adminRentalHistoryDetails, results.totalPages)
    }

    @Transactional
    fun updateRentalStatus(rentalHistoryId: Long, request: RentalStatusUpdateRequest) {
        val rentalHistory = rentalRepository.findById(rentalHistoryId)
            .orElseThrow { ApiException(RentalErrorCode.RENTAL_NOT_FOUND) }
        val renter = rentalHistory.member

        rentalHistory.updateStatus(request.rentalStatus)

        val itemName = rentalHistory.item.name

        when (rentalHistory.rentalStatus) {
            RentalStatus.CONFIRMED -> {
                //승인
                notificationService.sendNotification(
                    renter,
                    NotificationStatus.USER_RENTAL_APPROVED,
                    listOf(
                        itemName
                    ),
                    true
                )
            }

            RentalStatus.REJECTED -> {
                //대여 반려
                notificationService.sendNotification(
                    renter,
                    NotificationStatus.USER_RENTAL_REJECTED,
                    listOf(
                        itemName
                    ),
                    true
                )
            }

            RentalStatus.RETURN_CONFIRMED -> {
                //반납 승인
                notificationService.sendNotification(
                    renter,
                    NotificationStatus.USER_RETURN_APPROVED,
                    listOf(
                        itemName
                    ),
                    true
                )
            }

            RentalStatus.RETURNED -> {
                //반납 완료
                notificationService.sendNotification(
                    renter,
                    NotificationStatus.USER_RETURN_COMPLETED,
                    listOf(
                        itemName
                    )
                )
            }

            else -> return
        }
    }

    companion object {
        private val DASHBOARD_STATUS = listOf(RentalStatus.PENDING, RentalStatus.RETURN_PENDING)
        private val RETURN_REQUIRED_STATUS =
            listOf(RentalStatus.RENTAL, RentalStatus.RETURN_PENDING, RentalStatus.RETURN_CONFIRMED)
    }
}