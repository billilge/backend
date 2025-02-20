package site.billilge.api.backend.domain.rental.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.util.*

interface RentalRepository: JpaRepository<RentalHistory, Long?> {
    fun findByMemberId(memberId: Long) : List<RentalHistory>
    fun findByItemIdAndMemberIdAndRentalStatus(itemId:Long, memberId: Long, rentalStatus: RentalStatus): Optional<RentalHistory>
    fun findByMemberIdAndRentalStatus(memberId: Long, rentalStatus: RentalStatus): List<RentalHistory>
    fun findByMemberIdAndRentalStatusIn(
        memberId: Long,
        rentalStatuses: Collection<RentalStatus>
    ): List<RentalHistory>

    fun findAllByRentalStatusIn(rentalStatuses: List<RentalStatus>): List<RentalHistory>

    @Query("SELECT h FROM RentalHistory h WHERE h.member.name LIKE CONCAT('%', :memberName, '%')")
    fun findAllByMemberNameContaining(memberName: String, pageable: Pageable): Page<RentalHistory>
}