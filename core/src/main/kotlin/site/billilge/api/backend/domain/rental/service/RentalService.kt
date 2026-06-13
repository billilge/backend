package site.billilge.api.backend.domain.rental.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.common.utils.isWeekend
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.configvalue.enums.ConfigValueKeys
import site.billilge.api.backend.domain.configvalue.service.ConfigValueService
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.rental.appender.RentalAppender
import site.billilge.api.backend.domain.rental.appender.RentalStatusWorkerLogAppender
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.entity.RentalStatusWorkerLog
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.event.RentalAppliedEvent
import site.billilge.api.backend.domain.rental.event.RentalCancelledEvent
import site.billilge.api.backend.domain.rental.event.RentalStatusChangedEvent
import site.billilge.api.backend.domain.rental.event.ReturnAppliedEvent
import site.billilge.api.backend.domain.rental.exception.RentalErrorCode
import site.billilge.api.backend.domain.rental.reader.RentalReader
import site.billilge.api.backend.domain.rental.reader.RentalStatusWorkerLogReader
import site.billilge.api.backend.domain.rental.remover.RentalRemover
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
@Transactional(readOnly = true)
class RentalService(
    private val rentalReader: RentalReader,
    private val rentalAppender: RentalAppender,
    private val rentalRemover: RentalRemover,
    private val rentalStatusWorkerLogAppender: RentalStatusWorkerLogAppender,
    private val rentalStatusWorkerLogReader: RentalStatusWorkerLogReader,
    private val eventPublisher: ApplicationEventPublisher,
    private val configValueService: ConfigValueService,
    private val itemService: ItemService,
) {
    @Transactional
    fun createRental(rentUser: Member, item: Item, count: Int, rentAt: LocalDateTime, ignoreDuplicate: Boolean, isDevMode: Boolean = false) {
        validatePayer(rentUser)
        validateStock(count, item.count)

        if (!ignoreDuplicate) checkDuplicateRental(item.id!!, rentUser.id!!)

        if (isDevMode && rentUser.role != Role.ADMIN) throw ApiException(MemberErrorCode.FORBIDDEN)
        if (!isDevMode) validateRentalTime(rentAt)

        rentalAppender.save(
            RentalHistory(id = null, member = rentUser, item = item, rentalStatus = RentalStatus.PENDING,
                rentedCount = count, rentAt = rentAt)
        )
        eventPublisher.publishEvent(RentalAppliedEvent(rentUser.id!!, item.name, rentAt, isDevMode))
    }

    @Transactional
    fun createRentalByAdmin(worker: Member, rentUser: Member, item: Item, count: Int, rentAt: LocalDateTime): RentalHistory {
        validateStock(count, item.count)
        if (!rentUser.isFeePaid) throw ApiException(RentalErrorCode.MEMBER_IS_NOT_PAYER_ADMIN)

        val status = if (item.type == ItemType.RENTAL) RentalStatus.RENTAL else RentalStatus.RETURNED
        val returnedAt = if (status == RentalStatus.RETURNED) LocalDateTime.now() else null

        val saved = rentalAppender.save(
            RentalHistory(id = null, member = rentUser, item = item, rentalStatus = status,
                rentedCount = count, rentAt = rentAt, returnedAt = returnedAt)
        )
        itemService.adjustCount(item.id!!, -count)
        rentalStatusWorkerLogAppender.save(
            RentalStatusWorkerLog(id = null, rentalHistoryId = saved.id!!, rentalStatus = status, worker = worker)
        )
        return saved
    }

    @Transactional
    fun updateRentalStatus(worker: Member, rentalHistoryId: Long, rentalStatus: RentalStatus) {
        val rentalHistory = rentalReader.read(rentalHistoryId)
        val item = rentalHistory.item

        if (rentalStatus == RentalStatus.CONFIRMED && item.count <= 0)
            throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)

        val newStatus = if (rentalStatus == RentalStatus.RENTAL && item.type == ItemType.CONSUMPTION)
            RentalStatus.RETURNED else rentalStatus

        val returnedAt = if (newStatus == RentalStatus.RETURNED) LocalDateTime.now() else rentalHistory.returnedAt
        val updatedWorker = if (newStatus == RentalStatus.CONFIRMED) worker else rentalHistory.worker

        when (newStatus) {
            RentalStatus.CONFIRMED -> {
                if (rentalHistory.rentedCount > item.count) throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)
                itemService.adjustCount(item.id!!, -rentalHistory.rentedCount)
            }
            RentalStatus.RETURNED -> {
                if (item.type != ItemType.CONSUMPTION) itemService.adjustCount(item.id!!, +rentalHistory.rentedCount)
                else return
            }
            RentalStatus.REJECTED, RentalStatus.RETURN_CONFIRMED -> Unit
            else -> return
        }

        rentalAppender.save(rentalHistory.copy(rentalStatus = newStatus, returnedAt = returnedAt, worker = updatedWorker))
        eventPublisher.publishEvent(RentalStatusChangedEvent(rentalHistory.member.id!!, item.name, newStatus))
        rentalStatusWorkerLogAppender.save(
            RentalStatusWorkerLog(id = null, rentalHistoryId = rentalHistoryId, rentalStatus = newStatus, worker = worker)
        )
    }

    @Transactional
    fun cancelRental(memberId: Long, rentalHistoryId: Long) {
        val rentalHistory = rentalReader.read(rentalHistoryId)
        rentalAppender.save(rentalHistory.copy(rentalStatus = RentalStatus.CANCEL))
        eventPublisher.publishEvent(RentalCancelledEvent(rentalHistory.member.id!!, rentalHistory.item.name))
    }

    @Transactional
    fun returnRental(memberId: Long, rentalHistoryId: Long) {
        val rentalHistory = rentalReader.read(rentalHistoryId)
        rentalAppender.save(rentalHistory.copy(rentalStatus = RentalStatus.RETURN_PENDING))
        eventPublisher.publishEvent(ReturnAppliedEvent(rentalHistory.member.id!!, rentalHistory.item.name))
    }

    @Transactional
    fun updateItemCode(rentalHistoryId: Long, itemCode: String) {
        val rentalHistory = rentalReader.read(rentalHistoryId)
        rentalAppender.save(rentalHistory.copy(itemCode = itemCode))
    }

    @Transactional
    fun deleteRentalHistory(rentalHistoryId: Long) = rentalRemover.remove(rentalHistoryId)

    fun getMemberRentalHistory(memberId: Long, rentalStatus: RentalStatus?): List<RentalHistory> =
        if (rentalStatus == null) rentalReader.readByMemberId(memberId)
        else rentalReader.readByMemberIdAndStatus(memberId, rentalStatus)

    fun getReturnRequiredItems(memberId: Long): List<RentalHistory> =
        rentalReader.readByMemberIdAndStatusIn(memberId, RETURN_REQUIRED_STATUS)

    fun getAllDashboardApplications(rentalStatus: RentalStatus?): List<RentalHistory> =
        rentalReader.readAllByStatusIn(DASHBOARD_STATUS)
            .filter { if (rentalStatus == null) true else it.rentalStatus == rentalStatus }

    fun getAllRentalHistories(pageRequest: PageRequest, search: String): PageResult<RentalHistory> =
        rentalReader.readAllByMemberNameContaining(search, pageRequest)

    fun getWorkerLogs(rentalHistoryId: Long) =
        rentalStatusWorkerLogReader.readAllByRentalHistoryId(rentalHistoryId)

    private fun validatePayer(member: Member) {
        if (!member.isFeePaid) throw ApiException(RentalErrorCode.MEMBER_IS_NOT_PAYER)
    }

    private fun validateStock(rentedCount: Int, availableCount: Int) {
        if (rentedCount > availableCount) throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)
    }

    private fun checkDuplicateRental(itemId: Long, memberId: Long) {
        if (rentalReader.readByItemIdAndMemberIdAndStatus(itemId, memberId, RentalStatus.RENTAL) != null)
            throw ApiException(RentalErrorCode.RENTAL_ITEM_DUPLICATED)
    }

    private fun validateRentalTime(rentAt: LocalDateTime) {
        if (rentAt.toLocalDate().isInExamPeriod) throw ApiException(RentalErrorCode.TODAY_IS_IN_EXAM_PERIOD)
        if (rentAt.isWeekend) throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_WEEKEND)
        if (rentAt.isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul"))))
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_PAST)
        if (rentAt.hour < 10 || rentAt.hour > 17)
            throw ApiException(RentalErrorCode.INVALID_RENTAL_TIME_OUT_OF_RANGE)
    }

    private val LocalDate.isInExamPeriod: Boolean
        get() {
            val config = configValueService.getMapByKeys(
                listOf(ConfigValueKeys.EXAM_PERIOD_START_DATE.key, ConfigValueKeys.EXAM_PERIOD_END_DATE.key)
            )
            val start = LocalDate.parse(config[ConfigValueKeys.EXAM_PERIOD_START_DATE.key] ?: return false)
            val end = LocalDate.parse(config[ConfigValueKeys.EXAM_PERIOD_END_DATE.key] ?: return false)
            return this in (start..end)
        }

    companion object {
        private val DASHBOARD_STATUS = listOf(
            RentalStatus.PENDING, RentalStatus.RETURN_PENDING,
            RentalStatus.RETURN_CONFIRMED, RentalStatus.CONFIRMED,
        )
        private val RETURN_REQUIRED_STATUS =
            listOf(RentalStatus.RENTAL, RentalStatus.RETURN_PENDING, RentalStatus.RETURN_CONFIRMED)
    }
}
