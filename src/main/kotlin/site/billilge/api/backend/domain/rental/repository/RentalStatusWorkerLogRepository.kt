package site.billilge.api.backend.domain.rental.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.rental.entity.RentalStatusWorkerLog

interface RentalStatusWorkerLogRepository : JpaRepository<RentalStatusWorkerLog, Long?> {
    fun findAllByRentalHistoryIdOrderByCreatedAtAsc(rentalHistoryId: Long): List<RentalStatusWorkerLog>
}
