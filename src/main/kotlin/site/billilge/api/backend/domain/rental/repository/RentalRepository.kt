package site.billilge.api.backend.domain.rental.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.util.*

interface RentalRepository: JpaRepository<RentalHistory, Long?> {
    fun findByItemIdAndMemberIdAndRentalStatus(itemId:Long, memberId: Long, rentalStatus: RentalStatus): Optional<RentalHistory>
    fun findByMemberIdAndRentalStatus(memberId: Long, rentalStatus: RentalStatus): List<RentalHistory>
}