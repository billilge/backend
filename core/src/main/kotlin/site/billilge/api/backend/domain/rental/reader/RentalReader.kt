package site.billilge.api.backend.domain.rental.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.exception.RentalErrorCode
import site.billilge.api.backend.domain.rental.repository.RentalRepository

@Component
class RentalReader(private val rentalRepository: RentalRepository) {

    fun read(id: Long): RentalHistory =
        rentalRepository.findById(id) ?: throw ApiException(RentalErrorCode.RENTAL_NOT_FOUND)

    fun readByMemberId(memberId: Long): List<RentalHistory> =
        rentalRepository.findByMemberId(memberId)

    fun readByItemIdAndMemberIdAndStatus(itemId: Long, memberId: Long, status: RentalStatus): RentalHistory? =
        rentalRepository.findByItemIdAndMemberIdAndRentalStatus(itemId, memberId, status)

    fun readByMemberIdAndStatus(memberId: Long, status: RentalStatus): List<RentalHistory> =
        rentalRepository.findByMemberIdAndRentalStatus(memberId, status)

    fun readByMemberIdAndStatusIn(memberId: Long, statuses: Collection<RentalStatus>): List<RentalHistory> =
        rentalRepository.findByMemberIdAndRentalStatusIn(memberId, statuses)

    fun readAllByStatusIn(statuses: List<RentalStatus>): List<RentalHistory> =
        rentalRepository.findAllByRentalStatusIn(statuses)

    fun readAllByMemberNameContaining(memberName: String, pageRequest: PageRequest): PageResult<RentalHistory> =
        rentalRepository.findAllByMemberNameContaining(memberName, pageRequest)
}
