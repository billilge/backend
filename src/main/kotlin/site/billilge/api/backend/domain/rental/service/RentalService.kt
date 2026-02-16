package site.billilge.api.backend.domain.rental.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.notification.enums.NotificationStatus
import site.billilge.api.backend.domain.notification.service.NotificationService
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.exception.RentalErrorCode
import site.billilge.api.backend.domain.rental.repository.RentalRepository
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.utils.isWeekend
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
@Transactional(readOnly = true)
class RentalService(
    private val rentalRepository: RentalRepository,

    private val notificationService: NotificationService,

    @Value("\${exam-period.start-date}")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private val examPeriodStartDate: LocalDate,

    @Value("\${exam-period.end-date}")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private val examPeriodEndDate: LocalDate,
) {
    @Transactional
    fun createRental(rentUser: Member, item: Item, count: Int, rentAt: LocalDateTime, ignoreDuplicate: Boolean, isDevMode: Boolean = false) {
        validatePayer(rentUser)
        validateStock(count, item.count)

        if (!ignoreDuplicate) {
            checkDuplicateRental(item.id!!, rentUser.id!!)
        }

        if (isDevMode && rentUser.role != Role.ADMIN)
            throw ApiException(MemberErrorCode.FORBIDDEN)

        if (!isDevMode) {
            validateRentalTime(rentAt)
        }

        val newRental = RentalHistory(
            member = rentUser,
            item = item,
            rentalStatus = RentalStatus.PENDING,
            rentedCount = count,
            rentAt = rentAt
        )

        rentalRepository.save(newRental)

        notificationService.sendNotification(
            rentUser,
            NotificationStatus.USER_RENTAL_APPLY,
            listOf(item.name),
            true
        )

        if (!isDevMode) {
            notificationService.sendNotificationToAdmin(
                NotificationStatus.ADMIN_RENTAL_APPLY,
                listOf(
                    rentUser.name,
                    rentUser.studentId,
                    "${String.format("%02d", rentAt.hour)}:${String.format("%02d", rentAt.minute)}",
                    item.name
                ),
                true
            )
        }
    }

    @Transactional
    fun createRentalByAdmin(rentUser: Member, item: Item, count: Int, rentAt: LocalDateTime) {
        validatePayer(rentUser)
        validateStock(count, item.count)

        val newRental = RentalHistory(
            member = rentUser,
            item = item,
            rentalStatus = if (item.type == ItemType.RENTAL) RentalStatus.RENTAL else RentalStatus.RETURNED,
            rentedCount = count,
            rentAt = rentAt
        ).apply {
            if (rentalStatus == RentalStatus.RETURNED) {
                returnedAt = LocalDateTime.now()
            }
        }

        rentalRepository.save(newRental)
    }

    @Transactional
    fun deleteRentalHistory(rentalHistoryId: Long) {
        rentalRepository.deleteById(rentalHistoryId)
    }

    fun getMemberRentalHistory(memberId: Long?, rentalStatus: RentalStatus?): List<RentalHistory> {
        return if (rentalStatus == null) {
            rentalRepository.findByMemberId(memberId!!)
        } else {
            rentalRepository.findByMemberIdAndRentalStatus(memberId!!, rentalStatus)
        }
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

    fun getReturnRequiredItems(memberId: Long?): List<RentalHistory> {
        return rentalRepository.findByMemberIdAndRentalStatusIn(memberId!!, RETURN_REQUIRED_STATUS)
    }

    fun getAllDashboardApplications(rentalStatus: RentalStatus?): List<RentalHistory> {
        return rentalRepository.findAllByRentalStatusIn(DASHBOARD_STATUS)
            .filter { if (rentalStatus == null) true else it.rentalStatus == rentalStatus }
    }

    fun getAllRentalHistories(
        pageableCondition: PageableCondition,
        searchCondition: SearchCondition
    ): Page<RentalHistory> {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.DESC, pageableCondition.criteria ?: "applicatedAt")
        )
        return rentalRepository.findAllByMemberNameContaining(searchCondition.search, pageRequest)
    }

    @Transactional
    fun updateRentalStatus(worker: Member, rentalHistoryId: Long, rentalStatus: RentalStatus) {
        val rentalHistory = rentalRepository.findById(rentalHistoryId)
            .orElseThrow { ApiException(RentalErrorCode.RENTAL_NOT_FOUND) }
        val renter = rentalHistory.member
        val item = rentalHistory.item
        val rentedCount = rentalHistory.rentedCount

        if (rentalStatus == RentalStatus.CONFIRMED && item.count <= 0) {
            throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)
        }

        val newStatus =
            if (rentalStatus == RentalStatus.RENTAL && item.type == ItemType.CONSUMPTION)
                RentalStatus.RETURNED
            else rentalStatus

        rentalHistory.updateStatus(newStatus)

        val itemName = item.name

        when (rentalHistory.rentalStatus) {
            RentalStatus.CONFIRMED -> {
                //승인
                if (rentedCount > item.count) {
                    throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)
                }

                item.subtractCount(rentalHistory.rentedCount)
                rentalHistory.updateWorker(worker)

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
                if (item.type == ItemType.CONSUMPTION) return

                item.addCount(rentalHistory.rentedCount)
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

    private fun validatePayer(member: Member) {
        if (!member.isFeePaid)
            throw ApiException(RentalErrorCode.MEMBER_IS_NOT_PAYER)
    }

    private fun validateStock(rentedCount: Int, availableCount: Int) {
        if (rentedCount > availableCount)
            throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)
    }

    private fun checkDuplicateRental(itemId: Long, memberId: Long) {
        val rentalHistory = rentalRepository.findByItemIdAndMemberIdAndRentalStatus(
            itemId, memberId, RentalStatus.RENTAL
        )
        if (rentalHistory.isPresent)
            throw ApiException(RentalErrorCode.RENTAL_ITEM_DUPLICATED)
    }

    private fun validateRentalTime(rentAt: LocalDateTime) {
        val today = rentAt.toLocalDate()
        if (today.isInExamPeriod)
            throw ApiException(RentalErrorCode.TODAY_IS_IN_EXAM_PERIOD)

        if (rentAt.isWeekend)
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_WEEKEND)

        val currentKoreanTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        if (rentAt.isBefore(currentKoreanTime))
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_PAST)

        if (rentAt.hour < 10 || rentAt.hour > 17)
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_OUT_OF_RANGE)
    }

    private val LocalDate.isInExamPeriod
        get() = this in (examPeriodStartDate..examPeriodEndDate)

    companion object {
        private val DASHBOARD_STATUS = listOf(
            RentalStatus.PENDING,
            RentalStatus.RETURN_PENDING,
            RentalStatus.RETURN_CONFIRMED,
            RentalStatus.CONFIRMED
        )
        private val RETURN_REQUIRED_STATUS =
            listOf(RentalStatus.RENTAL, RentalStatus.RETURN_PENDING, RentalStatus.RETURN_CONFIRMED)
    }
}
