package site.billilge.api.backend.domain.rental.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus

interface RentalRepository {
    fun findById(id: Long): RentalHistory?
    fun save(rentalHistory: RentalHistory): RentalHistory
    fun delete(id: Long)
    fun findByMemberId(memberId: Long): List<RentalHistory>
    fun findByItemIdAndMemberIdAndRentalStatus(
        itemId: Long,
        memberId: Long,
        rentalStatus: RentalStatus,
    ): RentalHistory?
    fun findByMemberIdAndRentalStatus(memberId: Long, rentalStatus: RentalStatus): List<RentalHistory>
    fun findByMemberIdAndRentalStatusIn(
        memberId: Long,
        rentalStatuses: Collection<RentalStatus>,
    ): List<RentalHistory>
    fun findAllByRentalStatusIn(rentalStatuses: List<RentalStatus>): List<RentalHistory>
    fun findAllByMemberNameContaining(memberName: String, pageable: Pageable): Page<RentalHistory>
}
